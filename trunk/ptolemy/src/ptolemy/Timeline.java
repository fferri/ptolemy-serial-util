package ptolemy;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyMinimumViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.painters.TracePainterLine;
import info.monitorenter.util.Range;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The timeline view
 *
 * @author Federico Ferri <federico.ferri.it@gmail.com>
 */
public class Timeline extends JFrame {

	public static enum Index {
		SENSOR_HI,
		SENSOR_LO,
		REFERENCE_HI,
		REFERENCE_LO,
		COMPUTED
	}

	public Timeline() {
		super("data");

		initCharts();
		initTraces();

		/*for(traceIdx = 0; traceIdx < tracesz; traceIdx++) {
		for(int i = 0; i < traceidx.length; i++) {
		traces[i].addPoint(traceIdx, 0);
		}
		}*/

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		if (useChart1) {
			getContentPane().add(chart1);
		}

		/*scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);

		scrollPaneContent = new JPanel();
		scrollPaneContent.setLayout(new BoxLayout(scrollPaneContent, BoxLayout.Y_AXIS));
		scrollPane.add(scrollPaneContent);*/

		for (int i = 0; i < charts.length; i++) {
			getContentPane().add(charts[i]);
			//scrollPaneContent.add(charts[i]);
		}

		setSize(700, 500);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				//disablePeriodicRead();
			}
		});
	}

	private void initCharts() {
		axes = new AAxis[1];

		axes[0] = new AxisLinear();

		if (useChart1) {
			chart1 = new Chart2D();
			chart1.setBackground(Color.black);
			chart1.setForeground(Color.gray);
			chart1.addAxisYLeft(axes[0]);
		}

		int n = Index.values().length;
		charts = new Chart2D[n];
		for (int i = 0; i < n; i++) {
			charts[i] = new Chart2D();
			charts[i].setBackground(Color.black);
			charts[i].setForeground(Color.gray);
			charts[i].setUseAntialiasing(true);
		}

		charts[Index.SENSOR_HI.ordinal()].getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(0,128)));
		charts[Index.SENSOR_LO.ordinal()].getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(0,32)));
		charts[Index.REFERENCE_HI.ordinal()].getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(0,128)));
		charts[Index.REFERENCE_LO.ordinal()].getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(0,32)));
		charts[Index.COMPUTED.ordinal()].getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(0,0.25)));
	}

	private void initTraces() {
		// limited number of values:
		int tracesz = 60;
		Index traceidx[] = Index.values();

		traces = new ITrace2D[traceidx.length];
		String traceName[] = new String[traceidx.length];
		traceName[0] = "VSon";
		traceName[1] = "VSoff";
		traceName[2] = "VRon";
		traceName[3] = "VRoff";
		traceName[4] = Application.getApplication().getConfig().getComputeFormula();

		for (int i = 0; i < traceidx.length; i++) {
			traces[i] = new Trace2DLtd(tracesz, traceName[i]);
		}

		for (Index i : Index.values()) {
			initTrace(i);
		}
	}

	private void initTrace(Index i) {
		int j = i.ordinal();

		addTraceToChart(traces[j], i);

		//traces[j].setTracePainter(new Timeline.TracePainter());
		traces[j].setColor(traceColor[j]);

		if (i.equals(Index.COMPUTED)) {
			traces[j].setStroke(new BasicStroke(3));
		}
	}

	private void addTraceToChart(ITrace2D t, Index i) {
		int j = i.ordinal();

		if (useChart1) {
			if (i.equals(Index.COMPUTED)) {
				chart1.addTrace(t, chart1.getAxisX(), axes[0]);
			} else {
				chart1.addTrace(t);
			}
		}

		charts[j].addTrace(t);
	}

	public void addPoint(Index i, Double d) {
		traces[i.ordinal()].addPoint(traceIdx, d);


		if (i.equals(Index.COMPUTED)) {
			traceIdx++;
		}
	}

	public void clear() {
		for (ITrace2D t : traces) {
			t.removeAllPoints();
		}
	}

	public void setChartVisible(Index i, boolean v) {
		Chart2D theChart = charts[i.ordinal()];
		theChart.setVisible(v);
	}
	
	private static final boolean useChart1 = false;
	private JScrollPane scrollPane;
	private JPanel scrollPaneContent;
	private Chart2D chart1;
	private Chart2D charts[];
	private ITrace2D traces[];
	private Color traceColor[] = {Color.green, Color.cyan, Color.orange, Color.yellow, Color.red};
	private int traceIdx = 0;
	private AAxis axes[];

	public static class TracePainter extends TracePainterLine {

		@Override
		public void paintPoint(int absoluteX, int absoluteY, int nextX, int nextY, Graphics g, ITracePoint2D original) {
			super.paintPoint(absoluteX, absoluteY, absoluteX, nextY, g, original);
			super.paintPoint(absoluteX, nextY, nextX, nextY, g, original);
		}
	}
}
