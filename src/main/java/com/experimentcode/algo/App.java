package com.experimentcode.algo;

/**
 * Hello world!
 *
 */
public class App 
{
	public native long getObjectSize();
	String test="Rohan";
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.load("C:\\Users\\Rohan\\eclipse-cpp-workspace\\JVMCProfilingAgent\\JAVAProfilingTool\\Default\\libJAVAProfilingTool.exe");
        App app = new App();
        long objectSize = app.getObjectSize();
        System.out.println("Size:"+objectSize);
    }
}
