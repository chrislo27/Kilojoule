package chrislo27.kilojoule.init;

import ionium.registry.GlobalVariables;
import ionium.util.DebugSetting;

public class IoniumEngineTweaks {

	public static final void tweak() {
		GlobalVariables.instance().putInt("TICKS", 20);
		GlobalVariables.instance().putString("VERSION_URL",
				"https://raw.githubusercontent.com/chrislo27/VersionPlace/master/Kilojoule-version.txt");

		DebugSetting.showFPS = false;
	}

}
