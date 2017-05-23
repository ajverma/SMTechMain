package munchie.layout;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {
	public IPageLayout layout1 = null;
	 
	public IPageLayout getLayout1() {
		return layout1;
	}

	public void setLayout1(IPageLayout layout1) {
		this.layout1 = layout1;
	}

	public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        layout.setFixed(false);
        layout.setEditorAreaVisible(false);
        layout.addStandaloneView("smart_machine.munchies.views.MunchiesView",  true, 
        		IPageLayout.TOP, 0.25f, layout.getEditorArea());
//        layout.addStandaloneView("smart_machine.buddy.views.BuddyView",  true, 
//        		IPageLayout.BOTTOM, 0.75f, layout.getEditorArea());
        setLayout1(layout);
	}
}
