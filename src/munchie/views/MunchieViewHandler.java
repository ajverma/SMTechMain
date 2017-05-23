package munchie.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class MunchieViewHandler extends AbstractHandler{
	public Object execute(final ExecutionEvent event) throws ExecutionException {
//		IWorkbenchPart part = HandlerUtil.getActivePart(event);
//	    Shell shell = part.getSite().getShell();
//	    org.eclipse.swt.graphics.Point size = shell.getSize();
//	    shell.setSize(size.x + 100, size.y + 100);
	    return null;
	}

	public Object getActiveEditorDetails () {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window == null ? null : window.getActivePage();
		IEditorPart activeEditorDetails = page == null ? null : page.getActiveEditor();
		return activeEditorDetails;
	}
}