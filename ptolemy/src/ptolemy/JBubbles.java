package ptolemy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Federico Ferri
 */
public class JBubbles extends JPanel {
	private class Particle {
		public double x, y;
		public double vx, vy;
		public double ax, ay;
		public double radius;
	}

	private class AnimationTask implements Runnable {
		private boolean run = true;

		public void run() {
			double MV = decay ? 20 : 8;
			while(run) {
				for (Particle a : particle) {
					a.ax = (decay ? 0.31 : 0.0835) * (center.x - a.x) / (decay ? 1 : a.radius);
					a.ay = (decay ? 0.31 : 0.0835) * (center.y - a.y) / (decay ? 1 : a.radius);
					a.vx = Math.max(-MV, Math.min(MV, a.vx + a.ax));
					a.vy = Math.max(-MV, Math.min(MV, a.vy + a.ay));
					if(decay) {
						// noise
						a.x += 9 * (Math.random() - 0.5);
						a.y += 9 * (Math.random() - 0.5);
					}
					a.x = (a.x + a.vx);
					a.y = (a.y + a.vy);
				}
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							repaint();
						}
					});
					Thread.sleep((int)(1000.0/24));
				} catch (InterruptedException ex) {
				} catch (InvocationTargetException ex) {
				}
			}
		}

		public void stop() {
			run = false;
		}
	}
	
	private static final int MAX_PARTICLES = 30;
	private Particle particle[] = new Particle[MAX_PARTICLES];
	private Particle center;
	private AnimationTask animationTask = null;
	private boolean decay = false;

	public JBubbles() {
		init();

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				mouseMoved(e);
			}

			public void mouseMoved(MouseEvent e) {
				center.x += (e.getX() - center.x) / 2;
				center.y += (e.getY() - center.y) / 2;
			}
		});

		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				//decay = !decay;
			}

			public void mousePressed(MouseEvent e) {
				decay = true;
			}

			public void mouseReleased(MouseEvent e) {
				decay = false;
			}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {
				center.x = 82;
				center.y = 97;
			}
		});
	}

	public final void init() {
		center = new Particle();
		center.x = 82;
		center.y = 97;

		for(int i = 0; i < MAX_PARTICLES; i++) {
			particle[i] = new Particle();
			particle[i].x = center.x + (Math.random() - 0.5) * 64;
			particle[i].y = center.y + (Math.random() - 0.5) * 84;
			particle[i].vx = 10 * (Math.random() - 0.5);
			particle[i].vy = 10 * (Math.random() - 0.5);
			particle[i].radius = 2 + Math.random() * 9;
		}
	}

	public final void startAnimation() {
		if(animationTask != null) return;
		animationTask = new AnimationTask();
		new Thread(animationTask).start();
	}

	public final void stopAnimation() {
		if(animationTask == null) return;
		animationTask.stop();
		animationTask = null;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(Particle c : particle) {
			//int r = (int)(2 + 0.5 * c.vx * c.vx + 0.5 * c.vy * c.vy);
			int r = (int) c.radius;
			float h = (float)Math.min(1.0, Math.max(0, Math.atan(Math.sqrt(0.05 * c.vx * c.vx + 0.05 * c.vy * c.vy))*2.0f/3.14159f));
			g.setColor(new Color((float) Math.pow(h, 0.3), h * h, 0.0f));
			g.fillOval((int) c.x - r/2, (int) c.y - r/2, r, r);
		}
	}
}
