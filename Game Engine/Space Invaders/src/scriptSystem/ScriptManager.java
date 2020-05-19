package scriptSystem;

import java.io.FileNotFoundException;

import javax.script.*;

public class ScriptManager {
	private static ScriptEngine js_engine = new ScriptEngineManager().getEngineByName("JavaScript");
	private static Invocable js_invocable = (Invocable) js_engine;
	
	public static void bindArgument(String name, Object obj) {
		js_engine.put(name, obj);
	}
	
	public static void loadScript(String script_name) {
		try {
			js_engine.eval(new java.io.FileReader(script_name));
		} catch (FileNotFoundException | ScriptException e) {
			e.printStackTrace();
		}
	}
	
	// execute a function specified with the function_name with no parameter
	public static void executeScript(String function_name) {
		try {
			js_invocable.invokeFunction(function_name);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}
	
	// execute a function specified with the function_name with args parameters
	public static void executeScript(String function_name, Object... args) {
		try {
			js_invocable.invokeFunction(function_name, args);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}
}
