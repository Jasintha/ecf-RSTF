package org.eclipse.ecf.testframework.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({"org.eclipse.ecf.testframework.newannotaions.PrintTest"})
public class AnnotationProcessor  extends AbstractProcessor {

	  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
	  {
	    Messager messager = processingEnv.getMessager();

	    for (TypeElement te : annotations)
	    {
	      for (Element e : env.getElementsAnnotatedWith(te))
	      {
	        messager.printMessage(Diagnostic.Kind.NOTE, "Name of the method: " + e.toString());
	      }
	    }
	    return true;
	  }

	  @Override
	  public SourceVersion getSupportedSourceVersion()
	  {
	    return SourceVersion.latestSupported();
	  }

}
