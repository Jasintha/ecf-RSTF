package org.eclipse.ecf.remoteservice.testframework.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.IContainerManager;
import org.eclipse.ecf.remoteservice.testframework.Activator;
import org.eclipse.ecf.remoteservice.testframework.handler.ServiceRegisterHandler;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;


public class FrameworkEditorPage extends FormPage {
	private Text text;
	private Text text_1;
	private Text text_2;
	private Table table;
	private Combo combo;
	private Button btnLocalhost;
	private ServiceRegisterHandler handler;
	String[] itmes;
	private Text txtContainerProviderShould;
	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public FrameworkEditorPage(String id, String title) {
		super(id, title);
 
	}

	/**
	 * Create the form page.
	 * @param editor
	 * @param id
	 * @param title
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter id "Some id"
	 * @wbp.eval.method.parameter title "Some title"
	 */
	public FrameworkEditorPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("ECF R-OSGi Testing  Framework (1.0.0)");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(1, false));
		new Label(managedForm.getForm().getBody(), SWT.NONE);
		
		Section sctnNewSection = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnNewSection = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_sctnNewSection.heightHint = 270;
		gd_sctnNewSection.widthHint = 669;
		sctnNewSection.setLayoutData(gd_sctnNewSection);
		managedForm.getToolkit().paintBordersFor(sctnNewSection);
		sctnNewSection.setText("Service Containers ");
		
		Composite composite = new Composite(sctnNewSection, SWT.NONE);
		managedForm.getToolkit().adapt(composite);
		managedForm.getToolkit().paintBordersFor(composite);
		sctnNewSection.setClient(composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblSelectServiceContainer = new Label(composite, SWT.NONE);
		managedForm.getToolkit().adapt(lblSelectServiceContainer, true, true);
		lblSelectServiceContainer.setText("Select a Service Container ");
		
		combo = new Combo(composite, SWT.NONE);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 156;
		combo.setLayoutData(gd_combo);
		managedForm.getToolkit().adapt(combo);
		managedForm.getToolkit().paintBordersFor(combo);
		new Label(composite, SWT.NONE);
		
		Group grpDefineNewService = new Group(composite, SWT.NONE);
		grpDefineNewService.setText("Define new Service Containers");
		grpDefineNewService.setLayout(new GridLayout(2, false));
		GridData gd_grpDefineNewService = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpDefineNewService.heightHint = 101;
		gd_grpDefineNewService.widthHint = 423;
		grpDefineNewService.setLayoutData(gd_grpDefineNewService);
		managedForm.getToolkit().adapt(grpDefineNewService);
		managedForm.getToolkit().paintBordersFor(grpDefineNewService);
		
		Label lblServiceContinerDiscriptionname = new Label(grpDefineNewService, SWT.NONE);
		managedForm.getToolkit().adapt(lblServiceContinerDiscriptionname, true, true);
		lblServiceContinerDiscriptionname.setText("service container description");
		
		text_1 = new Text(grpDefineNewService, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_1.widthHint = 202;
		text_1.setLayoutData(gd_text_1);
		managedForm.getToolkit().adapt(text_1, true, true);
		
		Label lblConsumerContainerDiscription = new Label(grpDefineNewService, SWT.NONE);
		lblConsumerContainerDiscription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		managedForm.getToolkit().adapt(lblConsumerContainerDiscription, true, true);
		lblConsumerContainerDiscription.setText("Consumer container description");
		
		text_2 = new Text(grpDefineNewService, SWT.BORDER);
		GridData gd_text_2 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_2.widthHint = 193;
		text_2.setLayoutData(gd_text_2);
		managedForm.getToolkit().adapt(text_2, true, true);
		
		Button btnNewButton_2 = new Button(grpDefineNewService, SWT.NONE);
		GridData gd_btnNewButton_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_2.widthHint = 91;
		btnNewButton_2.setLayoutData(gd_btnNewButton_2);
		managedForm.getToolkit().adapt(btnNewButton_2, true, true);
		btnNewButton_2.setText("Add  ");
		new Label(grpDefineNewService, SWT.NONE);
		new Label(managedForm.getForm().getBody(), SWT.NONE);
		
		Section sctnServiceHostsLoactions = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnServiceHostsLoactions = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2);
		gd_sctnServiceHostsLoactions.heightHint = 131;
		gd_sctnServiceHostsLoactions.widthHint = 666;
		sctnServiceHostsLoactions.setLayoutData(gd_sctnServiceHostsLoactions);
		managedForm.getToolkit().paintBordersFor(sctnServiceHostsLoactions);
		sctnServiceHostsLoactions.setText("Service Hosts Loactions");
		sctnServiceHostsLoactions.setExpanded(true);
		
		Composite composite_1 = new Composite(sctnServiceHostsLoactions, SWT.NONE);
		managedForm.getToolkit().adapt(composite_1);
		managedForm.getToolkit().paintBordersFor(composite_1);
		sctnServiceHostsLoactions.setClient(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		btnLocalhost = new Button(composite_1, SWT.CHECK);
		btnLocalhost.setSelection(true);
		managedForm.getToolkit().adapt(btnLocalhost, true, true);
		btnLocalhost.setText("localhost");
		
		Group grpDefineARemote = new Group(composite_1, SWT.NONE);
		grpDefineARemote.setText("Define a Remote Service");
		grpDefineARemote.setLayout(new GridLayout(2, false));
		GridData gd_grpDefineARemote = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpDefineARemote.heightHint = 52;
		gd_grpDefineARemote.widthHint = 513;
		grpDefineARemote.setLayoutData(gd_grpDefineARemote);
		managedForm.getToolkit().adapt(grpDefineARemote);
		managedForm.getToolkit().paintBordersFor(grpDefineARemote);
		
		Label lblServiceIp = new Label(grpDefineARemote, SWT.NONE);
		managedForm.getToolkit().adapt(lblServiceIp, true, true);
		lblServiceIp.setText("Service URL");
		
		text = new Text(grpDefineARemote, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 173;
		text.setLayoutData(gd_text);
		managedForm.getToolkit().adapt(text, true, true);
		btnNewButton_2.addListener(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				if(text_1.getText()!=null&& text_2.getText()!=null&&text_1.getText().equals("")&& text_2.getText().equals("")){
					try{
						createIContainer(text_1.getText());
						createIContainer(text_2.getText());
						createPropery(text_1.getText(),text_1.getText(),text_2.getText());
						combo.removeAll();
						try {
							combo.setItems(getContainerList());
						} catch (Exception e) {
						   e.printStackTrace();
						}
						text_1.setText("");
						text_2.setText("");
					 }catch(Exception e){
						 txtContainerProviderShould.setText(e.getMessage());
					}
				}
			}
		});
		new Label(managedForm.getForm().getBody(), SWT.NONE);
		
		Composite composite_5 = new Composite(managedForm.getForm().getBody(), SWT.NONE);
		composite_5.setLayout(new GridLayout(1, false));
		GridData gd_composite_5 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_5.widthHint = 596;
		gd_composite_5.heightHint = 74;
		composite_5.setLayoutData(gd_composite_5);
		managedForm.getToolkit().adapt(composite_5);
		managedForm.getToolkit().paintBordersFor(composite_5);
		
		Group grpStartTestting = new Group(composite_5, SWT.NONE);
		grpStartTestting.setLayout(new GridLayout(1, false));
		GridData gd_grpStartTestting = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpStartTestting.widthHint = 454;
		gd_grpStartTestting.heightHint = 59;
		grpStartTestting.setLayoutData(gd_grpStartTestting);
		grpStartTestting.setText("Start Testting ");
		managedForm.getToolkit().adapt(grpStartTestting);
		managedForm.getToolkit().paintBordersFor(grpStartTestting);
		
		Button btnNewButton = new Button(grpStartTestting, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 81;
		btnNewButton.setLayoutData(gd_btnNewButton);
		managedForm.getToolkit().adapt(btnNewButton, true, true);
		btnNewButton.setText("Start");
		btnNewButton.addListener(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
			  startTest();
			}
		});
		
		try {
			combo.setItems(this.getContainerList());
			combo.select(0);
			new Label(composite, SWT.NONE);
			
			txtContainerProviderShould = new Text(composite, SWT.READ_ONLY | SWT.MULTI);
			txtContainerProviderShould.setText("Container provider should be avilable inorder to define a new container ");
			txtContainerProviderShould.setEditable(false);
			GridData gd_txtContainerProviderShould = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
			gd_txtContainerProviderShould.widthHint = 477;
			gd_txtContainerProviderShould.heightHint = 71;
			txtContainerProviderShould.setLayoutData(gd_txtContainerProviderShould);
			managedForm.getToolkit().adapt(txtContainerProviderShould, true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startTest(){
		try{
		String serviceContainer = combo.getItem(combo.getSelectionIndex());
		if(serviceContainer!=null){
			setServiceContainers(serviceContainer);
			if(btnLocalhost.getSelection()){
			     handler.setHostId("local");
			     handler.testInit();
				}else{
					if(text.getText()!=null){
						handler.setHostId(text.getText());
						handler.testInit();
					}
		       }
		}
		}catch(Exception e){
		 
		}
	  }
 
	
	private void setServiceContainers(String symbollicName) throws URISyntaxException, IOException{
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL fileURL = bundle.getEntry("containers.properties");
		File file = new File(FileLocator.resolve(fileURL).toURI());
		Properties properties = new Properties();
		FileInputStream fileIn = new FileInputStream(file);
		properties.load(fileIn);
		fileIn.close();
		String property = properties.getProperty(symbollicName);
		String[] split = property.split(",");
	    handler.setServiceContainer(split[0]);
	    handler.setConsumerContainer(split[1]);
	}
	
	private void createPropery(String sybollicName,String service,String consumer){
		try {
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			URL fileURL = bundle.getEntry("containers.properties");
			File file = null;
			try {
			    file = new File(FileLocator.resolve(fileURL).toURI());
			} catch (URISyntaxException e1) {
			    e1.printStackTrace();
			} catch (IOException e1) {
			    e1.printStackTrace();
			}
			Properties properties = new Properties();
			
			FileInputStream fileIn = new FileInputStream(file);
			properties.load(fileIn);
			String containers = properties.getProperty("ContainerList");
			fileIn.close();
			containers = containers+","+sybollicName;
			properties.setProperty("ContainerList", containers);
			properties.setProperty(sybollicName,service+","+consumer);
 
			FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, "ecf service containers");
			fileOut.close();
		 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	
	private String[] getContainerList() throws Exception {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL fileURL = bundle.getEntry("containers.properties");
		File file = new File(FileLocator.resolve(fileURL).toURI());
		Properties properties = new Properties();
		FileInputStream fileIn = new FileInputStream(file);
		properties.load(fileIn);
		fileIn.close();
		String property = properties.getProperty("ContainerList");
		String[] split = property.split(",");
		return split;
	}
	
	private void createIContainer(String containerTypeDescriptionName) throws ContainerCreateException{
		BundleContext bundleContext = Platform.getBundle(Activator.PLUGIN_ID).getBundleContext(); 
		ServiceTracker serviceTracker = new ServiceTracker(bundleContext, IContainerManager.class.getName(),null);
		serviceTracker.open();
		IContainerManager manager = (IContainerManager) serviceTracker.getService();
		manager.getContainerFactory().createContainer(containerTypeDescriptionName);
	}
	
	
	public void setHandler(ServiceRegisterHandler handler) {
		this.handler = handler;
	}

	public ServiceRegisterHandler getHandler() {
		return handler;
	}

}
