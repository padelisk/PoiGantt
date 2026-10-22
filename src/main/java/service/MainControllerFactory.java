package service;

public class MainControllerFactory {

	public IMainController createMainController() {
		return new MainController();
		//TODO FIX: return a new instance of your IMainController implementation ;
	}
}
