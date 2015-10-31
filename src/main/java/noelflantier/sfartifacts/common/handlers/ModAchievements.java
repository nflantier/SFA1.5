package noelflantier.sfartifacts.common.handlers;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class ModAchievements {

	private static AchievementPage achievementsPage;
	private static HashMap<String, Achievement> achievementsList = new HashMap<String, Achievement>();

	public static void addAchievement (String name, Achievement achievement)
	{
		achievementsList.put(name, achievement.registerStat());
	}

	public static Achievement getAchievement (String name)
	{
		return achievementsList.get(name);
	}

	public static void triggerAchievement (EntityPlayer player, String name)
	{

		Achievement ach = getAchievement(name);

		if (ach != null)
		{
			player.triggerAchievement(ach);
		}
	}

	public static void addModAchievements()
	{
		addAchievement("sfartifacts.manual", new Achievement("sfartifacts.manual", "sfartifacts.manual", 0, 0, ModItems.itemManual, null).initIndependentStat());

	}

	public static void registerAchievementPane ()
	{
		Achievement[] achievements = new Achievement[achievementsList.size()];
		achievements = achievementsList.values().toArray(achievements);
		achievementsPage = new AchievementPage("SFArtifacts Achievements", achievements);
		AchievementPage.registerAchievementPage(achievementsPage);
	}
}