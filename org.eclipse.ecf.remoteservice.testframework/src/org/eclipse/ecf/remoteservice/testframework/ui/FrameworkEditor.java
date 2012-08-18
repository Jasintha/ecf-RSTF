package org.eclipse.ecf.remoteservice.testframework.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ecf.remoteservice.testframework.handler.ServiceRegisterHandler;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;



public class FrameworkEditor  extends FormEditor {

	private static FrameworkEditorPage editorPage;
	
	@Override
	protected void addPages() {
		
		editorPage = new FrameworkEditorPage(this,"org.eclipse.ecf.remoteservice.testframework.ui.editorID", "Test Configuretion");
			try {
				addPage(editorPage);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		
	}

	public static void setHandler(ServiceRegisterHandler handler){
		editorPage.setHandler(handler);
	}
	
	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
