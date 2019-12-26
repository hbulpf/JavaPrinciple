package dev.lpf.designpattern.adapter.objectAdapter;

/**
 * Target Role
 * @author foreverlpficloud.com
 *
 */
public interface ITarget {
	public String getUserName();
	public String getHomeAddress();
	public String getOfficeName();
	public void request();
}
