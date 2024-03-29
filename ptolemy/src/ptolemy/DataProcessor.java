package ptolemy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
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
	private static class DataRecord {
		public static String hdr = null;
		int a, b, c, d; double e;
		public DataRecord(int x, int y, int z, int w, double h) {a=x;b=y;c=z;d=w;e=h;}
		public String toString() {return String.format("%d;%d;%d;%d;%f", a, b, c, d, e); }
	}
	private LinkedList<DataRecord> allValues = new LinkedList<DataRecord>();

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
		Double comp = p.eval();
		
		if(comp.isInfinite()) comp = 0.0;

		comp = doMobileAverage(comp);
		
		DataRecord.hdr = "VSon;VSoff;VRon;VRoff;" + Application.getApplication().getConfig().getComputeFormula();
		allValues.add(new DataRecord(receivedData[2], receivedData[0], receivedData[3], receivedData[1], comp));

		Application.println("  COMP = " + comp);
		//traces[TraceIndex.COMPUTED.ordinal()].addPoint(traceIdx, comp);
		//traceIdx++;
		if (timeline != null) {
			timeline.addPoint(Timeline.Index.COMPUTED, comp.isInfinite() ? 0 : comp);
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
	
	public void exportCSVData(File f) throws Exception {
		PrintWriter br = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)));
		if(DataRecord.hdr != null) br.println(DataRecord.hdr);
		for(DataRecord r : allValues) br.println(r.toString());
		br.close();
	}
	
	public void clearAllData() {
		allValues.clear();
		DataRecord.hdr = null;
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
