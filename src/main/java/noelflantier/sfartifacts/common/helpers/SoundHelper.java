package noelflantier.sfartifacts.common.helpers;

public enum SoundHelper {
	ANVIL("random.anvil_use"),
	PORTALTRAVEL("portal.travel");
	
	public String sound;
	SoundHelper(String s){
		this.sound = s;
	}
}
