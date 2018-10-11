package designpattern.adapter.objectAdapter;

/**
 * Source Role 3
 * @author foreverlpficloud.com
 *
 */
public class AdapteeOffice {
	private String office;
	//business of old role
	public AdapteeOffice(String office) {
		this.office=office;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
}
