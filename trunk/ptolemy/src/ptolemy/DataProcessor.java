package ptolemy;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import ptolemy.exprparser.ExprParser;

/**
 *
 * @author Federico Ferri
 */
public class DataProcessor {
	private Timeline timeline;
	private LinkedList<Double> valuesQueue = new LinkedList<Double>();

	public DataProcessor() {
	}

	public void setTimeline(Timeline t) {
		timeline = t;
	}

	public void data(String prefix, int objNum, int objValue) {
		//Application.println("data: prefix=" + prefix + " objNum=" + objNum + " objValue=" + objValue);

		if (prefix.equals("ADC")) {
			switch (objNum) {
				case 0:
					dataSensor(objValue);
					break;
				case 1:
					dataReference(objValue);
					break;
			}
		} else if (prefix.equals("LED")) {
			switch (objNum) {
				case 2:
					ledOn = (objValue != 0);
					break;
			}
		}
	}
	private int receivedData[] = {-1, -1, -1, -1};
	private boolean ledOn = false;

	private void dataSensor(int val) {
		//Application.println("dataSensor: " + val + (ledOn ? " (LED: ON)" : " (LED: OFF)"));
		receivedData[0 + (ledOn ? 2 : 0)] = val;
		//traces[(ledOn ? TraceIndex.SENSOR_HI : TraceIndex.SENSOR_LO).ordinal()]
		//        .addPoint(traceIdx, val);
		if(timeline != null)
			timeline.addPoint(ledOn ? Timeline.Index.SENSOR_HI : Timeline.Index.SENSOR_LO, new Double(val));
		checkComputeData();
	}

	private void dataReference(int val) {
		//Application.println("dataReference: " + val + (ledOn ? " (LED: ON)" : " (LED: OFF)"));
		receivedData[1 + (ledOn ? 2 : 0)] = val;
		//traces[(ledOn ? TraceIndex.REFERENCE_HI : TraceIndex.REFERENCE_LO).ordinal()]
		//        .addPoint(traceIdx, val);
		if(timeline != null)
			timeline.addPoint(ledOn ? Timeline.Index.REFERENCE_HI : Timeline.Index.REFERENCE_LO, new Double(val));
		checkComputeData();
	}

	private void checkComputeData() {
		boolean haveAllData = true;
		for (int i = 0; i < receivedData.length; i++) {
			haveAllData = haveAllData && (receivedData[i] != -1);
		}

		if (haveAllData) {
			computedData();

			clearReceivedData();
		}
	}

	private void computedData() {
		//double comp = (receivedData[2] - receivedData[0]) * 1.0 / (receivedData[3] - receivedData[1]);
		ExprParser p = Application.getApplication().getCompExpr();
		p.setVar("VSon", receivedData[2]);
		p.setVar("VSoff", receivedData[0]);
		p.setVar("VRon", receivedData[3]);
		p.setVar("VRoff", receivedData[1]);
		double comp = p.eval();

		comp = doMobileAverage(comp);

		Application.println("  COMP = " + comp);
		//traces[TraceIndex.COMPUTED.ordinal()].addPoint(traceIdx, comp);
		//traceIdx++;
		if (timeline != null) {
			timeline.addPoint(Timeline.Index.COMPUTED, comp);
		}
	}

	private double doMobileAverage(double v) {
		valuesQueue.add(v);

		int sz = Application.getApplication().getConfig().getMobileMeanSize();
		while(valuesQueue.size() > sz)
			valuesQueue.remove();

		double r = 0.0;
		for(Double x : valuesQueue) r += x;
		r /= valuesQueue.size();

		return r;
	}

	private void clearReceivedData() {
		for (int i = 0; i < receivedData.length; i++) {
			receivedData[i] = -1;
		}
	}

	public void parseAnswerAndNotify(String a) {
		Pattern pADC = Pattern.compile("^([A-Z]+)(\\d+)=(\\d+)$");
		Matcher m = pADC.matcher(a);
		if (m.matches()) {
			String prefix = m.group(1);
			int objNum = Integer.parseInt(m.group(2));
			int objValue = Integer.parseInt(m.group(3));

			SwingUtilities.invokeLater(new RawDataPacket(prefix, objNum, objValue));
		}
	}

	public class RawDataPacket implements Runnable {
		private String prefix;
		private int objNum;
		private int objValue;

		public RawDataPacket(String prefix, int objNum, int objValue) {
			this.prefix = prefix;
			this.objNum = objNum;
			this.objValue = objValue;
		}

		public void run() {
			data(prefix, objNum, objValue);
		}
	}
}
