package gui.menu;

import javax.swing.UIManager;

import com.jtattoo.plaf.JTattooUtilities;

/**
 * Game launcher. Be careful, please use JTattoo, JFreeChart , JUnit5, Jcommon
 * .jar to launch the program.
 * 
 * @see JTattooUtilities
 * @author CHEF, MOA
 *
 */
public class LauncherMenu {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// select Look and Feel and call the program
			UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");
			PanelMenu.frameMenu();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
