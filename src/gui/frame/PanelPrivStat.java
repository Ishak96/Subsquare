package gui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import gui.fontElements.Fonts;

public class PanelPrivStat extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel prosperityLabel = new JLabel("District Prosperity", JLabel.CENTER);
	private JLabel price_information = new JLabel("");
	private JLabel position = new JLabel("");
	private JLabel typeDistrict = new JLabel("");
	private JLabel isSubwayStation = new JLabel("");
	private JLabel densityLabel = new JLabel("");
	
	
	private JPanel scorepan = new JPanel();
	private JPanel prosperity = new JPanel();
	private JProgressBar prosperityBar;
	private GridBagConstraints gbc = new GridBagConstraints();
	
	public PanelPrivStat() {
		super();
		
		setBorder(BorderFactory.createEtchedBorder());

		JPanel[] tabCells = { new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel(),
				new JPanel(), new JPanel(), new JPanel() };

		setLayout(new BorderLayout());
		// ---------------------------Setting and placement of the cells from the
		// GridBagLayout---------
		prosperity.setLayout(new GridBagLayout());
		prosperityBar = new JProgressBar(SwingConstants.VERTICAL);
		prosperityBar.setBackground(Color.DARK_GRAY);
		prosperityLabel.setFont(Fonts.getF3());
		price_information.setFont(Fonts.getF3());
		typeDistrict.setFont(Fonts.getF3());
		isSubwayStation.setFont(Fonts.getF3());
		densityLabel.setFont(Fonts.getF3());

		gbc.gridx = 0;
		gbc.gridy = 0;
		//gbc.fill = GridBagConstraints.VERTICAL;
		prosperityBar.setPreferredSize(new Dimension(200, 35));
		prosperity.add(prosperityBar, gbc);
		gbc.gridy = 1;
		prosperityLabel.setPreferredSize(new Dimension(150, 50));
		prosperity.add(prosperityLabel, gbc);
		gbc.gridy = 2;
		prosperity.add(price_information, gbc);
		gbc.gridy = 3;
		prosperity.add(position, gbc);
		gbc.gridy = 4;
		prosperity.add(typeDistrict, gbc);
		gbc.gridy = 5;
		prosperity.add(isSubwayStation, gbc);
		gbc.gridy = 6;
		prosperity.add(densityLabel, gbc);
		
		
		
		//prosperityLabel.setFont(Fonts.getF3());
		for (int index = 0; index < tabCells.length; index++) {
			tabCells[index].setPreferredSize(new Dimension(200, 30));
		}
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		prosperity.add(tabCells[0], gbc);
		gbc.gridy = 1;
		prosperity.add(tabCells[1], gbc);
		gbc.gridy = 2;
		prosperity.add(tabCells[2], gbc);
		gbc.gridy = 3;
		prosperity.add(tabCells[3], gbc);
		gbc.gridy = 4;
		prosperity.add(tabCells[4], gbc);
		gbc.gridy = 5;
		prosperity.add(tabCells[5], gbc);
		gbc.gridy = 6;
		prosperity.add(tabCells[6], gbc);
		
		
		scorepan.add(prosperity);
		add(scorepan, BorderLayout.CENTER);
		
		
		
	}
	
	public JProgressBar getProsperityBar() {
		return prosperityBar;
	}
	
	public void setposLabel(String s) {
		position.setText(s);
	}
	
	public void setTypeLabel(String type) {
		typeDistrict.setText(type);
	}
	
	public void setPriceInformation(String price) {
		this.price_information.setText(price);
	}
	
	public void setIsSubwayStation(String subway) {
		this.isSubwayStation.setText(subway);
	}
	
	public void setdensityLabel(String density) {
		this.densityLabel.setText(density);
	}

}