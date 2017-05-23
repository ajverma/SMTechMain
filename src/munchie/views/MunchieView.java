package munchie.views;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.WorkbenchPart;
import com.techm.ProMunchies.MessageModel.FileDetailsModel;
import com.techm.ProMunchies.MessageSendingCtrl.MunchieClientMsgCtrl;
 
/**
 * This view class is responsible for controlling plugin display
 */
public class MunchieView extends ViewPart{
	public static final String ID = "munchie.views.MunchieView";
	private TreeViewer munchieTree;
//	private Label label;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action doubleClickAction;
	private TreeParent invisibleRoot;
	private MunchieViewHandler handlerObj = new MunchieViewHandler();
	private FileDetailsModel fileDetailsObj = new FileDetailsModel();
	private MunchieClientMsgCtrl clientMsgCtrl = new MunchieClientMsgCtrl();
	private FormToolkit toolkit;
	
	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		public Object getAdapter(Class key) {
			return null;
		}
	}
	public class TreeParent extends TreeObject {
		private ArrayList children;
		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
		public Enumeration children() {return null;}
		public boolean getAllowsChildren() {return false;}
		public TreeNode getChildAt(int arg0) {return null;}
		public int getChildCount() {return 0;}
		public int getIndex(TreeNode arg0) {return 0;}
		public boolean isLeaf() {return false;}
		public void insert(MutableTreeNode arg0, int arg1) {}
		public void remove(int arg0) {}
		public void remove(MutableTreeNode arg0) {}
		public void removeFromParent() {}
		public void setParent(MutableTreeNode arg0) {}
		public void setUserObject(Object arg0) {}
	}
	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {}
		public void dispose() {}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null)initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
		private void initialize(){treeFormation();}
	}

	public void treeFormation(){
		invisibleRoot = new TreeParent("");
//		invisibleRoot.addChild(new TreeObject("Pro Munchies"));
		
		Response response = new Response();
		TreeParent parentFile = null;
		if(response.getFileName() != ""){
			for (int i = 0; i < response.getHostArrayList().size(); i++) {
				try {
					if (InetAddress.getLocalHost().getHostName().equals(
						response.getHostArrayList()
						.get(i))){
						parentFile = new TreeParent(extractFileName(response
								.getFileName()));
						parentFile = treeLabelling(response.getHostArrayList(), 
								response.getUserArrayList(), parentFile);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		} else {
			parentFile = new TreeParent("Nothing edited");
		}
		invisibleRoot.addChild(parentFile);
	}

	private String extractFileName(String fileName) {
		return new File(fileName).getAbsolutePath().substring(
				new File(fileName).getAbsolutePath().lastIndexOf("\\")+1);
	}

	class Response {
		public String getFileName() {
			if(handlerObj.getActiveEditorDetails() != null){
				
				return ((IEditorPart) handlerObj.getActiveEditorDetails()).getEditorInput()
					.getToolTipText();
				
			} else {
				return "";
			}
		}
		public ArrayList<String> getHostArrayList() {
			return new ArrayList<String>(Arrays.asList("INPUHJPC05868","ajay-7041",
					"akash-7042", "ashish-7043"));
		}
		public ArrayList<String> getUserArrayList() {
			return new ArrayList<>(Arrays.asList("anil","ajay", "akash","ashish"));
		}
	}	

	public TreeParent treeLabelling (ArrayList<String> hostsList,ArrayList<String> 
		usersList, TreeParent parent)
			throws UnknownHostException {
		for(int i=0; i < hostsList.size(); i++) {
			if(!InetAddress.getLocalHost().getHostName().equals(hostsList.get(i))) {
				parent.addChild(new TreeObject(hostsList.get(i) + " / " + 
						usersList.get(i)));
			}
		}
		return parent;
	}

	class ViewLabelProvider extends LabelProvider {
		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	
	class NameSorter extends ViewerSorter {}

	/*
	 * Constructor
	 */
	public MunchieView(){
		super();
		try {
			fileDetailsObj.setHostName(InetAddress.getLocalHost().getHostName());
			fileDetailsObj.setUserName(System.getProperty("user.name"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		hookToEditor();
	}

	public void hookToEditor() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService()
		.addPartListener(new IPartListener() {
		public void partOpened(IWorkbenchPart arg0) {}
		public void partDeactivated(IWorkbenchPart arg0) {}
		public void partClosed(IWorkbenchPart arg0) {
			treeFormation();
			munchieTree.refresh(true);
		}
		public void partActivated(IWorkbenchPart arg0) {}
		public void partBroughtToTop(IWorkbenchPart arg0) {
			if(handlerObj.getActiveEditorDetails() != null){
				fileDetailsObj.setFileName(((IEditorPart) handlerObj
						.getActiveEditorDetails()).getEditorInput().getToolTipText());
				treeFormation();
				munchieTree.refresh(true);
				((WorkbenchPart) handlerObj.getActiveEditorDetails()) 
				.addPropertyListener(new IPropertyListener() {
				public void propertyChanged(Object arg0, int arg1) {
					if(!PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActiveEditor().isDirty()){
						clientMsgCtrl.sendMessage(fileDetailsObj.getFileName(),
							fileDetailsObj.getHostName(), fileDetailsObj.getUserName());
						treeFormation();
						munchieTree.refresh(true);
					}
				}});
			}
		}});		
	}

	public void createPartControl(final Composite parent) {
//		parent.setSize(150, 500);
		
		Section section = new Section(parent, SWT.TOP);
		section.setText("Buddy");
		section.setToolTipText("Pro Buddy");
		section.setData("0", "");
//		section.setDescription("sdfsfdsfds,ncjdhf");
		section.setBackground(new org.eclipse.swt.graphics.Color(new Device() {
			public long internal_new_GC(GCData arg0) {return 0;}
			public void internal_dispose_GC(long arg0, GCData arg1) {}
		}, new RGB(185, 206, 196)));
//		section.setSize(150, 250);
		section.setExpanded(true);
		
		munchieTree = new TreeViewer(parent, SWT.BOTTOM);
		munchieTree.getControl().setToolTipText("Pro Munchies");
		munchieTree.getControl().setSize(150, 250);
		drillDownAdapter = new DrillDownAdapter(munchieTree);
		munchieTree.setContentProvider(new ViewContentProvider());
		munchieTree.setLabelProvider(new ViewLabelProvider());
		munchieTree.setSorter(new NameSorter());
		munchieTree.setInput(getViewSite());
		munchieTree.expandAll();
		
		
		Control[] munchiesTab = new Control[1];
		munchiesTab[0] = munchieTree.getControl();
		munchiesTab[0].setData(munchieTree);
		munchiesTab[0].setVisible(true);
//		munchiesTab[0].setSize(100, 100);
		munchiesTab[0].setFocus();
		munchiesTab[0].setLocation(0, 0);
//		munchiesTab[0].setBackground(new Color(new Device() {
//			public long internal_new_GC(GCData arg0) {return 0;}
//			public void internal_dispose_GC(long arg0, GCData arg1) {}
//		}, 38, 214, 130));
		
		Control[] buddyTab = new Control[1];
		buddyTab[0] = section.getDescriptionControl();
		buddyTab[0].setData("sdfsdf");
		buddyTab[0].setVisible(true);
		buddyTab[0].setSize(100, 100);
		buddyTab[0].setLocation(200, 200);
//		buddyTab[0].setBackground(new Color(new Device() {
//			public long internal_new_GC(GCData arg0) {return 0;}
//			public void internal_dispose_GC(long arg0, GCData arg1) {}
//		}, 211, 169, 124));
		
		parent.setTabList(munchiesTab);
		section.setTabList(buddyTab);
//		parent.setTabList(buddyTab);
		makeActions(); //TODO: To set future actions on tree labels 
		hookContextMenu();
		hookDoubleClickAction();
		
		
		
//		contributeToActionBars();
//	    b = new Button(parent, SWT.PUSH);
//	    b.setSize(0,0);
//	    b.addSelectionListener(new SelectionListener() {
//			public void widgetSelected(SelectionEvent arg0) {
//				EModelService s = (EModelService) getSite().getService(EModelService.class);
//			    MPartSashContainerElement p = (MPart) getSite().getService(MPart.class);
//			    if (p.getCurSharedRef() != null) {
//			    	p = p.getCurSharedRef();
//			    }
//			    s.detach(p, 500, 50, 200, 200);
//			}
//			public void widgetDefaultSelected(SelectionEvent arg0) {
//			}
//		});

//	    GridLayout layout = new GridLayout();
//	    tV = new ListViewer(parent);
//	    tV.setUseHashlookup(true);
//	    tV.add("Key");
//	    tV.add("Value");
	    
//		toolkit = new FormToolkit(parent.getDisplay());
//		Section munchiesSection = new Section(parent, parent.getStyle());
//	    munchiesSection.setText("Pro Munchies");
//	    munchiesSection.setToolTipText("Pro Munchies");
//	    
//	    Section buddySection = new Section(parent, parent.getStyle());
//	    buddySection.setText("Pro Buddy");
//	    buddySection.setToolTipText("Pro Buddy");
	    
		/*toolkit = new FormToolkit(parent.getDisplay());
	    Section section = toolkit.createSection(parent, Section.EXPANDED);
	    TableWrapData td = new TableWrapData();
	    Button button = new Button(section, section.getStyle());
	    Tree tree = new Tree(parent, getSite().getShell().getStyle());
	    td.colspan = 2;
	    section.setLayoutData(td);
	    section.setText("Pro Buddy");
	    Composite sectionClient = toolkit.createComposite(section);
	    sectionClient.setLayout(new GridLayout());	
	    
	    	 button = toolkit.createButton(sectionClient, "Radio 1", SWT.RADIO);
	    	 button = toolkit.createButton(sectionClient, "Radio 2", SWT.RADIO);
	    	 
	    section.setClient(sectionClient);*/
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MunchieView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(munchieTree.getControl());
		munchieTree.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, munchieTree);
	}

/*	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}
*/

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// TODO: Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1: Future scope");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = munchieTree.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		munchieTree.addDoubleClickListener(new IDoubleClickListener() {
			//TODO: compare file with repo
			public void doubleClick(DoubleClickEvent event) {
				showMessage(((WorkbenchPart) handlerObj.getActiveEditorDetails())
						.getTitle());
			}
		});
	}
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
			munchieTree.getControl().getShell(),
			"Munchie View",
			message);
	}

	public void setFocus() {
		munchieTree.getControl().setFocus();
	}
}




