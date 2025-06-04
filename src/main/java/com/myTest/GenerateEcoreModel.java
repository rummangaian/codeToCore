package com.myTest;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.impl.*;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.util.Collections;
import java.util.Map;

public class GenerateEcoreModel {
    public static void main(String[] args) throws Exception {
        // --- 1. Initialize EMF environment ---
        EcorePackage.eINSTANCE.eClass();
        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("ecore", new XMIResourceFactoryImpl());

        // --- 2. Create a new EPackage ---
        EcoreFactory ecoreFactory = EcoreFactory.eINSTANCE;
        EPackage myPackage = ecoreFactory.createEPackage();
        myPackage.setName("myModel");
        myPackage.setNsPrefix("myModel");
        myPackage.setNsURI("http://com.myTest.myModel");

        // --- 3. Define EClass “Person” ----
        EClass personClass = ecoreFactory.createEClass();
        personClass.setName("Person");

        EAttribute nameAttr = ecoreFactory.createEAttribute();
        nameAttr.setName("name");
        nameAttr.setEType(EcorePackage.Literals.ESTRING);
        nameAttr.setLowerBound(1);
        personClass.getEStructuralFeatures().add(nameAttr);

        EAttribute ageAttr = ecoreFactory.createEAttribute();
        ageAttr.setName("age");
        ageAttr.setEType(EcorePackage.Literals.EINT);
        ageAttr.setLowerBound(1);
        personClass.getEStructuralFeatures().add(ageAttr);

        // --- 4. Define EClass “Address” and a reference from Person ---
        EClass addressClass = ecoreFactory.createEClass();
        addressClass.setName("Address");

        EAttribute streetAttr = ecoreFactory.createEAttribute();
        streetAttr.setName("street");
        streetAttr.setEType(EcorePackage.Literals.ESTRING);
        streetAttr.setLowerBound(1);
        addressClass.getEStructuralFeatures().add(streetAttr);

        EAttribute cityAttr = ecoreFactory.createEAttribute();
        cityAttr.setName("city");
        cityAttr.setEType(EcorePackage.Literals.ESTRING);
        cityAttr.setLowerBound(1);
        addressClass.getEStructuralFeatures().add(cityAttr);

        // Create an EReference “address” in Person → Address
        EReference addressRef = ecoreFactory.createEReference();
        addressRef.setName("address");
        addressRef.setEType(addressClass);
        addressRef.setLowerBound(1);
        addressRef.setUpperBound(1);
        personClass.getEStructuralFeatures().add(addressRef);

        // --- 5. Add classes to the package ---
        myPackage.getEClassifiers().add(personClass);
        myPackage.getEClassifiers().add(addressClass);

        // --- 6. Register EPackage in the global registry ---
        EPackage.Registry.INSTANCE.put(myPackage.getNsURI(), myPackage);

        // --- 7. Create a Resource, add the package, and save as “myModel.ecore” ---
        ResourceSet resourceSet = new ResourceSetImpl();

        //  ←–– Change this to a path that definitely exists
        // For Windows, e.g.: "C:/Users/YourUserName/tmp/myModel.ecore"
        // For Linux/Mac, e.g.: "/home/yourname/tmp/myModel.ecore"
        URI fileURI = URI.createFileURI("/home/mobius/tmp/myModel.ecore");

        Resource resource = resourceSet.createResource(fileURI);
        resource.getContents().add(myPackage);
        resource.save(Collections.EMPTY_MAP);

        System.out.println("Saved Ecore model to: " + fileURI.toFileString());
    }
}
