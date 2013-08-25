package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Color;

import javax.swing.JLabel;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;

public class LabelView extends JLabel implements Listener<String> {

	
	public LabelView(Model<String> m){
		super("dupadupa");
		m.addListener(this);
		setOpaque(true);
		setBackground(Color.CYAN);
		setVisible(true);
		
	}

	@Override
	public void update(String data) {
		System.out.println("dupadupadupa");
		this.setText(data);
		invalidate();
		repaint();
		
	}
}
