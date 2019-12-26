package dev.lpf.designpattern.adapter.objectAdapter;

/**
 * Source Role 2
 * @author foreverlpficloud.com
 *
 */
public class AdapteeHome {
	private String home;
	//business of old role
	public AdapteeHome(String home) {
		this.home=home;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}

}
