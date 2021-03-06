package gui.frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import city.City;
import city.District;
import city.PrivateDistrict;
import city.PublicDistrict;
import city.ResidentialDistrict;
import economy.EcoData;
import economy.EconomyManager;
import engine.GridParameters;
import engine.Simulation;
import engine.TimeSimulator;
import used.Point;
import used.Random;

/**
 * This class allow to build the main frame of the game which will display the
 * map, all scores about a district and the city. It implements all action
 * buttons and launch the simulation class to operate on each data of the game.
 * 
 * @see Simulation {@link Simulation}
 * @see City {@link City}
 * @see Scene {@link Scene}
 * @see PanelPrivStat {@link PanelPrivStat}
 * @see PanelScore {@link PanelScore}
 *
 *@author CHEF, MOA RVR
 */
public class MainFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private static int THREAD_MAP = GridParameters.speed;
	private City city = City.getInstance();
	private static Scene scene = new Scene();
	private Simulation simulation;
	private boolean buildMetroLine_click = false, doSecondLine = false;
	private static boolean stop = true;

	private PanelScore pScore = new PanelScore();
	private PanelPrivStat pStat;

	private PanelAPI api = new PanelAPI();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu_game = new JMenu("Game");
	private JMenuItem item_save = new JMenuItem("Save");
	private JMenuItem item_load = new JMenuItem("Load a game");
	private JMenuItem item_manual = new JMenuItem("User's manual");
	private JMenuItem item_leave = new JMenuItem("Leave without save");

	private static Point position_districtA, position_dicstrictB;
	public static ArrayList<String> DistrictName = new ArrayList<String>();
	EconomyManager general_economy;

	/********* construct *********/
	public MainFrame() {
		super("Subsquare");
		setIconImage(new ImageIcon("subsquare_icon.png").getImage());
		setFocusable(true);
		simulation = new Simulation(GridParameters.getInstance());
		general_economy = simulation.getEcoManager();
		simulation.generatGrid();
		scene.setGrid(simulation.getGrid());
		generDistrictName("/districName/districNames.txt");
		init();
		launchGUI();
	}

	private void launchGUI() {
		stop = false;
		Thread chronoThread = new Thread(this);
		chronoThread.start();
	}

	public void init() {
		setResizable(false);
		getContentPane().setBackground(Color.darkGray);
		setSize(1650, 760);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		scene.setBounds(205, 5, 1185, 600);
		api.setBounds(200, 610, 1200, 125);
		pScore.setBounds(0, 0, 200, 1150);
		// pStat.setBounds(1400, 0, 250, 1150);

		this.menu_game.add(item_save);
		this.menu_game.add(item_load);
		this.menu_game.add(item_manual);
		this.menu_game.add(item_leave);

		// Action for leave without save
		item_leave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		item_manual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				new ManualFrame();
			}
		});

		this.menuBar.add(menu_game);
		this.setJMenuBar(menuBar);

		PanelScore.go.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (stop) {
					stop = false;
					launchGUI();
					GridParameters.setSpeed(800);
					THREAD_MAP = GridParameters.speed;
				}
			}
		});

		PanelScore.stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stop = true;
			}
		});

		PanelScore.fast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (GridParameters.speed > 0) {
					GridParameters.setSpeed(GridParameters.speed - 50);
					THREAD_MAP = GridParameters.speed;
				}
			}
		});
		/* When the user click, enter, exit, release or presse the mouse */
		scene.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				Point position = new Point(e.getX() / 28, e.getY() / 28);

				if (city.isDistrictPosition(position) && PanelAPI.getbuildMetroLine() && doSecondLine == true) {
					PanelAPI.setbuildMetroLine(false);
					setCursorOnScene(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					position_dicstrictB = new Point(position.getAbscisse(), position.getOrdonne());
					simulation.buildStation(position_dicstrictB);
					simulation.buildSubwayLine(position_districtA, position_dicstrictB);
					doSecondLine = false;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				/*
				 * When the user click on the panel Scene and if he press the API associated, he
				 * can draw a line on the map. If he click a second time, the builder is over
				 */
				Point position = new Point(e.getX() / 28, e.getY() / 28);

				if (city.isDistrictPosition(position) && PanelAPI.getbuildMetroLine()) {
					position_districtA = new Point(position.getAbscisse(), position.getOrdonne());
					simulation.buildStation(position_districtA);
					doSecondLine = true;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			/*
			 * Method to use when the user wants to interact with the map, that is to say,
			 * build a place, a line ...
			 */
			@SuppressWarnings("unused")
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				Point position = new Point(e.getX() / 28, e.getY() / 28);
				/*
				 * If the user wants to build a public district, the cursor appearance is
				 * changed, an area is built on the cursor location and if the user click, a
				 * district is build
				 */
				if (PanelAPI.getbuildPublicDistrict()) {
					simulation.buildDistrict(position, new PublicDistrict(),
							DistrictName.get(Random.randomInt(DistrictName.size(), false)));
					PanelAPI.setbuildPublicDistrict(false);
					District dis = City.getInstance().getDistrictByPosition(position);
					setCursorOnScene(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					scene.setDrawGrid(false);
				} else if (PanelAPI.getbuildPrivateDistrict()) {
					simulation.buildDistrict(position, new PrivateDistrict(),
							DistrictName.get(Random.randomInt(DistrictName.size(), false)));
					PanelAPI.setbuildPrivateDistrict(false);
					setCursorOnScene(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					scene.setDrawGrid(false);
					District dis = City.getInstance().getDistrictByPosition(position);
				} else if (PanelAPI.getbuildResidentialDistrict()) { // citizen created only in this district
					simulation.buildDistrict(position, new ResidentialDistrict(),
							DistrictName.get(Random.randomInt(DistrictName.size(), false)));
					District dis = City.getInstance().getDistrictByPosition(position);
					// simulation.creatCitizens(null, dis, true, 1);
					PanelAPI.setbuildResidentialDistrict(false);
					setCursorOnScene(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					scene.setDrawGrid(false);
				} else if (e.getClickCount() == 2) {
					scene.setDrawGrid(true);
					scene.setPos_gridPoint(position);
					pStat = new PanelPrivStat();
					pStat.setBounds(1400, 0, 250, 1150);

					// pStat.setposLabel("Position de ce quartier : " + position);
					if (scene.getGrid().getBoxAt(position.getOrdonne(), position.getAbscisse()).getGroundType()
							.isContainsTree()) {
						pStat.setLabelDistrict(1,
								"Prix de la zone :"+scene.getGrid().getBoxAt(position.getOrdonne(), position.getAbscisse()).getGroundType()
										.getDegre() * EcoData.CONST_DISTRICT * 4);
						// pStat.setPriceInformation("Prix de la zone : "
						// + scene.getGrid().getBoxAt(position.getOrdonne(), position.getAbscisse())
						// .getGroundType().getDegre() * EcoData.CONST_DISTRICT * 4);

					} else {
						pStat.setLabelDistrict(1,
								"Prix de la zone :"+scene.getGrid().getBoxAt(position.getOrdonne(), position.getAbscisse()).getGroundType()
										.getDegre() * EcoData.CONST_DISTRICT);
					}
					// pStat.setTypeLabel("Pas de type de quartier");
					getContentPane().add(pStat);
					/*
					 * In a nutshell, the user gotta pay a price if the place isn't free and have an
					 * obstacle
					 */

					/*
					 * When the place isn't free, a panel with all information about the
					 * position/district will be displayed
					 */
					if (city.isDistrictPosition(position)) {
						// pStat.setposLabel("Attention : cette place est occupée");
						// pStat.setPriceInformation("Prix :");
						// pStat.setTypeLabel(city.getDistrictByPosition(position).getType().toString());
						// pStat.setIsSubwayStation(
						// "Station de Métro : " + city.getDistrictByPosition(position).getStation());
						// pStat.setdensityLabelPriv(
						// "Population : " +
						// city.getDistrictByPosition(position).getType().getNbCitizens());
						pStat.setLabelDistrict(1, city.getDistrictByPosition(position).getName());
						pStat.setLabelDistrict(2, city.getDistrictByPosition(position).getType().toString());
						pStat.setLabelDistrict(3,
								"Population :" + city.getDistrictByPosition(position).getType().getNbCitizens());
						pStat.setLabelDistrict(4,
								"Workers :" + city.getDistrictByPosition(position).getType().getNbWorkers());
						pStat.setLabelDistrict(5,
								"Tax :" + city.getDistrictByPosition(position).getType().getTaxes() + "€");
						pStat.setLabelDistrict(6,
								"Unemployement :" + (int) city.getDistrictByPosition(position).getUnemployement());

						updateProsperityBacPrivate(city.getDistrictByPosition(position));
					}
				}
			}
		});
		/* When the user move or drag the mouse */
		scene.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				/* To draw the grid around an area and manage the line builder */
				Point position = new Point(e.getX() / 28, e.getY() / 28); // to know the exact position
				if (PanelAPI.getbuildPublicDistrict() || PanelAPI.getbuildPrivateDistrict()
						|| PanelAPI.getbuildResidentialDistrict() || PanelAPI.getbuildMetroLine()) {
					scene.setDrawGrid(true);
					scene.setPos_gridPoint(position);
				} else if (PanelAPI.getbuildMetroLine() == true && buildMetroLine_click == true) {
					/* Pour la partie line metro */
					/*
					 * Point line_position = new Point(e.getX() / 28, e.getY() / 28);
					 * scene.setLine(true); simulation.buildDistrict(line_position,new
					 * ResidentialDistrict());
					 */
				}

			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		/* Add content here to the panel */
		getContentPane().add(api);
		getContentPane().add(scene);
		getContentPane().add(pScore);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void updateGUI() {
		updateTime();
		updateBudget();
		updateTaxes();
		updateDensity();
		updateServicing();
		updateProsperityBar();

		if (pScore.getProsperityBar().getValue() >= 90) {
			stop = true;
			scene.win();
		} else if (pScore.getProsperityBar().getValue() <= 15 || general_economy.getBudget() <= 10) {
			stop = true;
			scene.game_over();
		}
		scene.updateUI();
		scene.repaint();
	}

	public void updateTime() {
		TimeSimulator timeSim = city.getTimeSimulator();
		timeSim.update();
		pScore.getDateField().setText(timeSim.getDate());
		pScore.getHourField().setText(timeSim.getTime());
	}

	public void updateTaxes() {
		pScore.getTaxesField().setText(general_economy.getTaxes() + " €/month");
	}

	public void updateBudget() {
		pScore.getBudgetField().setText(general_economy.getBudget() + " €");
	}

	public void updateDensity() {
		pScore.getDensityField().setText(city.getNbCitizens() + " inhabitants");
	}

	public void updateServicing() {
		pScore.getServicingField().setText(general_economy.getMaintenanceCost() + " €/month");
	}

	public void updateProsperityBar() {
		pScore.getProsperityBar().setValue((int) city.getProsperity());
	}

	public void updateProsperityBacPrivate(District district) {
		pStat.getProsperityBar().setValue((int) district.getProsperity());
	}

	@Override
	public void run() {
		while (!stop) {
			simulation.simulationNextTurn();
			updateGUI();
			try {
				Thread.sleep(THREAD_MAP);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void setThreadSpeed(int thread) {
		MainFrame.THREAD_MAP = thread;
	}

	public static void setStop(boolean stop) {
		MainFrame.stop = stop;
	}

	public static JPanel getScene() {
		return scene;
	}

	public static Point getPosition_districtA() {
		return position_districtA;
	}

	public static Point getPosition_dicstrictB() {
		return position_dicstrictB;
	}

	/* to change the cursor when an API is selected */
	public static void setCursorOnScene(Cursor c) {
		scene.setCursor(c);
	}

	public void generDistrictName(String title) {

		URL url = getClass().getResource(title);
		String ligne;
		try {
			URLConnection ucon = url.openConnection();
			BufferedReader read = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
			while ((ligne = read.readLine()) != null) {
				DistrictName.add(ligne);
			}
			read.close();
		} catch (Exception e) {

		}
	}

}