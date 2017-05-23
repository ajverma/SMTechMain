package munchie.views;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import com.techm.ProMunchies.MessageThreadCtrl.WorkerThread;

public class MunchiesStartup implements IStartup{
	public boolean pluginStarted = false;
	ExecutorService executor = Executors.newFixedThreadPool(1);
	
	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
			public void windowOpened(IWorkbenchWindow arg0) {}
			public void windowDeactivated(IWorkbenchWindow arg0) {}
			public void windowClosed(IWorkbenchWindow arg0) {}
			public void windowActivated(IWorkbenchWindow arg0) {
			if(PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getWorkbench()
				.addWindowListener(new IWindowListener() {
					public void windowOpened(IWorkbenchWindow arg0) {}
					public void windowDeactivated(IWorkbenchWindow arg0) {}
					public void windowClosed(IWorkbenchWindow arg0) {}
					public void windowActivated(IWorkbenchWindow arg0) {
						if(pluginStarted == false){
							//TODO: initiate munchies
						for(int i = 0; i<4; i++) {
							Runnable worker = new WorkerThread("zxczxc"); 
							executor.execute(worker);	
						}
						executor.shutdown();
						}
						pluginStarted = true;
					}
				});
			}
			}
		});
	}
}