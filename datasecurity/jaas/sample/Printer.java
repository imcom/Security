package sample;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.security.auth.Subject;

public class Printer implements PrivilegedAction<Printer>{
	
	private final Permission print_perm = new RuntimePermission("accessClassInPackage.Services.Print");
	private final Permission queue_perm = new RuntimePermission("accessClassInPackage.Services.Queue");
	private final Permission topQueue_perm = new RuntimePermission("accessClassInPackage.Services.TopQueue");
	private final Permission start_perm = new RuntimePermission("accessClassInPackage.Services.Start");
	private final Permission stop_perm = new RuntimePermission("accessClassInPackage.Services.Stop");
	private final Permission restart_perm = new RuntimePermission("accessClassInPackage.Services.Restart");
	private final Permission status_perm = new RuntimePermission("accessClassInPackage.Services.Status");
	private final Permission readConfig_perm = new RuntimePermission("accessClassInPackage.Services.ReadConfig");
	private final Permission setConfig_perm = new RuntimePermission("accessClassInPackage.Services.SetConfig");
	private Services service;
	private List<Permission> perms = new ArrayList<Permission>(9);
	
	private void getCaller() {

		AccessControlContext acc = AccessController.getContext();
    	Subject caller = Subject.getSubject(acc);
    	// let's see what Principals we have
    	Iterator<Principal> principalIterator = caller.getPrincipals().iterator();
    	System.out.println("\n\t=======================\nCaller has following Principals: \n");
    	while (principalIterator.hasNext()) {
    	    Principal p = (Principal)principalIterator.next();
    	    System.out.println("** Principal: " + p.toString());
    	}
	}
	
	public Printer() {
		perms.add(print_perm);
		perms.add(queue_perm);
		perms.add(readConfig_perm);
		perms.add(restart_perm);
		perms.add(start_perm);
		perms.add(stop_perm);
		perms.add(setConfig_perm);
		perms.add(topQueue_perm);
		perms.add(status_perm);
	}

	@Override
	public Printer run() {
		for (Permission perm : perms) {
			try {
				AccessController.checkPermission(perm);
				String service_name = perm.getName();
				this.getCaller();
				switch (service_name) {
					case "accessClassInPackage.Services.Print":
						service = new Print();
						break;
					case "accessClassInPackage.Services.Queue":
						service = new Queue();
						break;
					case "accessClassInPackage.Services.TopQueue":
						service = new TopQueue();
						break;
					case "accessClassInPackage.Services.Start":
						service = new Start();
						break;
					case "accessClassInPackage.Services.Stop":
						service = new Stop();
						break;
					case "accessClassInPackage.Services.Restart":
						service = new Restart();
						break;
					case "accessClassInPackage.Services.Status":
						service = new Status();
						break;
					case "accessClassInPackage.Services.ReadConfig":
						service = new ReadConfig();
						break;
					case "accessClassInPackage.Services.SetConfig":
						service = new SetConfig();
						break;
					default:
						break;
				}
				service.service();
			} catch (AccessControlException e) {
				System.out.println("Access denied for: " + e.getPermission().getName());
			}
		}
		return null;
	}
}