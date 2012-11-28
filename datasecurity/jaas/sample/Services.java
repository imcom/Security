package sample;

public interface Services {
	public void service();
}

class Print implements Services {

	private final String methodName = "print";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called \n");
	}
}

class Queue implements Services {

	private final String methodName = "queue";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called\n");
	}
}

class TopQueue implements Services {

	private final String methodName = "topQueue";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called\n");
	}
}

class Start implements Services {

	private final String methodName = "start";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called \n");
	}
}

class Stop implements Services {

	private final String methodName = "stop";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called\n");
	}
}

class Restart implements Services {

	private final String methodName = "restart";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called\n");
	}
}

class Status implements Services {

	private final String methodName = "status";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called \n");
	}
}

class ReadConfig implements Services {

	private final String methodName = "readConfig";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called\n");
	}
}

class SetConfig implements Services {

	private final String methodName = "setConfig";

	@Override
	public void service() {
		// TODO Auto-generated method stub
		System.out.println("\n\t{" + this.methodName + "} is called\n");
	}
}