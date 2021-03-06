# JPF-Symbc-RT
This is the Real-Time extension to Symbolic PathFinder which allows for generating a Network of Timed Automata (NTA) system model amenable to model checking using UPPAAL.
It allows Best Case Execution Time analysis as well as Worst Case Execution Time analysis to be conducted for Java Methods.
SPF-RT can be used "as is" i.e. like a normal extension to JPF where a .jpf configuration file is written, or it can be used through SymRT, a tool we have built for more elaborate timing analysis of real-time Java. SymRT and accompanying documentation can be found on http://people.cs.aau.dk/~luckow/symrt

# Installation:
Being an extension project to SPF, SPF-RT requires the following:
* jpf-core v7
* jpf-symbc v7
* UPPAAL

Prior to using SPF-RT, these should be properly installed. A non-commercial, free of charge version of UPPAAL can be obtained on www.uppaal.com. Please consult the projects' respective installation instructions.
There are only a few configurations that should be made for making the extension work; first, locate the site.properties file (usually this is located in ~/jpf/site.properties). Here you should add the absolute path to the real-time extension similarly to how jpf-symbc was added. In my case, this would be: 
jpf-symbc-rt = ${user.home}/workspace/jpf-symbc-rt

Then, add the extension to jpf extension variable in the site.properties file:
`extensions=${jpf-core},${jpf-symbc},${jpf-symbc-rt}`

# Setting up jpf-symbc-rt
For inspiration, look in some of the accompanying .jpf configuration files located in e.g. ${jpf-symbc-rt}/src/examples.
The entry point to SPF-RT is the UppaalTranslationListener, a listener that subscribes to certain events during the run of JPF. You would therefore need to specify this in your .jpf configuration file:

* `listener = gov.nasa.jpf.symbc.realtime.UppaalTranslationListener`

The next essential component is specifying the target method of the analysis. We adopt this configuration from SPF, hence this will specified as e.g.

symbolic.method = gov.nasa.jpf.symbc.realtime.algorithms.BubbleSort.runBubbleSort()
If the method has parameters, these can be assigned symbolic values using the notation "sym" with "#" being the separator.

The listener can be configured to reflect the purpose of the analysis:

* `symbolic.realtime.platform 			=	[jop|agnostic|timingdoc]	(default: jop)`
Since the timing of the target Java method is dependent on the underlying execution environment, this option allows specifying this. "jop" is the Java Optimized Processor for which all Java Bytecode execution times a known a priori. "agnostic" generates a generic timing model that relies on other automata for "simulating" this. These can be obtained from the TetaSARTS and TetaJ projects. "timingdoc" specifies that a timing document is provided stating the execution times of the Java Bytecode instructions. A timing doc is an XML document following a simple structure - see the TetaSARTS project for more on this. When using this option, the path to the timing doc must be provided as well using the configuration: symbolic.realtime.timingdoc.path = <source path>

* `symbolic.realtime.symrt 			=	[true|false]				(default: false)`
This option should be used if the timing model is to be used with SymRT. SymRT accepts the output of SPF-RT to generate a refined timing model that can be used for schedulability analysis, Worst Case Blocking Time analysis (WCBT), WCET, BCET, and processor utilisation and idle time analysis.

* `symbolic.realtime.outputbasepath 	=	<output path>				(default: ./)`
This option controls the output path of the resulting UPPAAL model file.

* `symbolic.realtime.optimize 			= 	[true|false]				(default: true)`
You can use this option to optimize the timing model. As of the current version, the optimizations only apply when the execution times of the Java Bytecode instructions are known i.e. when either "jop" or "timingdoc" is specified as the platform. The optimizations will significantly reduce state space size and as a result of this, analysis time and memory consumption during analysis.

* `symbolic.realtime.generatequeries 	= 	[true|false]				(default: true)`
Use this option to automatically generate the UPPAAL queries for conducting the analyses as reachability problems.

* `symbolic.realtime.progressmeasure	= 	[true|false]				(default: true)`
Generate UPPAAL progress measure to speed up verification and reduce memory requirements
