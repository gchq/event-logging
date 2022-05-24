//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.hisrc.jsonix.compilation.jsonschema;

import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.ClassInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.CreateTypeInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.EnumLeafInfoProducer;
import org.hisrc.jsonix.compilation.jsonschema.typeinfo.TypeInfoProducer;
import org.hisrc.jsonix.definition.Mapping;
import org.hisrc.jsonix.definition.Module;
import org.hisrc.jsonix.definition.Modules;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;

import javax.json.JsonBuilderFactory;
import javax.xml.namespace.QName;
import java.util.Comparator;
import java.util.Iterator;

public class JsonSchemaMappingCompiler<T, C extends T> {
    private final Modules<T, C> modules;
    private final Module<T, C> module;
    private final Mapping<T, C> mapping;
    private final JsonBuilderFactory jsonBuilderFactory;

    public JsonSchemaMappingCompiler(JsonBuilderFactory jsonBuilderFactory, Modules<T, C> modules, Module<T, C> module, Mapping<T, C> mapping) {
        Validate.notNull(jsonBuilderFactory);
        Validate.notNull(modules);
        Validate.notNull(module);
        Validate.notNull(mapping);
        this.jsonBuilderFactory = jsonBuilderFactory;
        this.modules = modules;
        this.module = module;
        this.mapping = mapping;
    }

    public Modules<T, C> getModules() {
        return this.modules;
    }

    public Module<T, C> getModule() {
        return this.module;
    }

    public Mapping<T, C> getMapping() {
        return this.mapping;
    }

    public JsonBuilderFactory getJsonBuilderFactory() {
        return this.jsonBuilderFactory;
    }

    public JsonSchemaBuilder compile() {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        String schemaId = this.mapping.getSchemaId();
        schema.addId(schemaId);
        this.addElementInfos(schema);
        this.addClassInfoSchemas(schema);
        this.addEnumLeafInfoSchemas(schema);
        return schema;
    }

    private void addElementInfos(JsonSchemaBuilder schema) {


        mapping.getElementInfos()
                .stream()
                .sorted(Comparator.comparing(t -> t.getElementName().toString()))
                .forEach(elementInfo -> {
                    QName elementName = elementInfo.getElementName();
                    MTypeInfo<T, C> typeInfo = elementInfo.getTypeInfo();
                    MClassInfo<T, C> scope = elementInfo.getScope();
                    JsonSchemaBuilder elementInfoSchema = new JsonSchemaBuilder();
                    elementInfoSchema.addType("object");
                    JsonSchemaBuilder qNameRef = (new JsonSchemaBuilder()).addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF);
                    JsonSchemaBuilder nameConstant = new JsonSchemaBuilder();
                    nameConstant.addType("object");
                    nameConstant.addProperty(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, (new JsonSchemaBuilder()).addEnum(elementName.getLocalPart()));
                    nameConstant.addProperty(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, (new JsonSchemaBuilder()).addEnum(elementName.getNamespaceURI()));
                    elementInfoSchema.addProperty(JsonixConstants.NAME_PROPERTY_NAME, (new JsonSchemaBuilder()).addAllOf(qNameRef).addAllOf(nameConstant));
                    elementInfoSchema.addProperty(JsonixConstants.VALUE_PROPERTY_NAME, (JsonSchemaBuilder) typeInfo.acceptTypeInfoVisitor(new CreateTypeInfoSchema(this, elementInfo)));
                    elementInfoSchema.add(JsonixJsonSchemaConstants.ELEMENT_NAME_PROPERTY_NAME, (new JsonSchemaBuilder()).add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, elementName.getLocalPart()).add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, elementName.getNamespaceURI()));
                    if (scope != null) {
                        elementInfoSchema.add(JsonixJsonSchemaConstants.SCOPE_PROPERTY_NAME, this.getTypeInfoProducer(elementInfo, typeInfo).createTypeInfoSchemaRef(this));
                    }
                });

//        JsonSchemaBuilder elementInfoSchema;
//        for (Iterator i$ = this.mapping.getElementInfos().iterator(); i$.hasNext(); schema.addAnyOf(elementInfoSchema)) {
//            MElementInfo<T, C> elementInfo = (MElementInfo) i$.next();
//            QName elementName = elementInfo.getElementName();
//            MTypeInfo<T, C> typeInfo = elementInfo.getTypeInfo();
//            MClassInfo<T, C> scope = elementInfo.getScope();
//            elementInfoSchema = new JsonSchemaBuilder();
//            elementInfoSchema.addType("object");
//            JsonSchemaBuilder qNameRef = (new JsonSchemaBuilder()).addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF);
//            JsonSchemaBuilder nameConstant = new JsonSchemaBuilder();
//            nameConstant.addType("object");
//            nameConstant.addProperty(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, (new JsonSchemaBuilder()).addEnum(elementName.getLocalPart()));
//            nameConstant.addProperty(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, (new JsonSchemaBuilder()).addEnum(elementName.getNamespaceURI()));
//            elementInfoSchema.addProperty(JsonixConstants.NAME_PROPERTY_NAME, (new JsonSchemaBuilder()).addAllOf(qNameRef).addAllOf(nameConstant));
//            elementInfoSchema.addProperty(JsonixConstants.VALUE_PROPERTY_NAME, (JsonSchemaBuilder) typeInfo.acceptTypeInfoVisitor(new CreateTypeInfoSchema(this, elementInfo)));
//            elementInfoSchema.add(JsonixJsonSchemaConstants.ELEMENT_NAME_PROPERTY_NAME, (new JsonSchemaBuilder()).add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, elementName.getLocalPart()).add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, elementName.getNamespaceURI()));
//            if (scope != null) {
//                elementInfoSchema.add(JsonixJsonSchemaConstants.SCOPE_PROPERTY_NAME, this.getTypeInfoProducer(elementInfo, typeInfo).createTypeInfoSchemaRef(this));
//            }
//        }

    }

    private void addEnumLeafInfoSchemas(JsonSchemaBuilder schema) {
        mapping.getEnumLeafInfos()
                .stream()
                .sorted(Comparator.comparing(t -> t.getContainerLocalName(".")))
                .forEach(enumLeafInfo -> {
                    EnumLeafInfoProducer<T, C> enumLeafInfoCompiler = new EnumLeafInfoProducer(enumLeafInfo);
                    JsonSchemaBuilder enumLeafInfoSchema = enumLeafInfoCompiler.compile(this);
                    schema.addDefinition(enumLeafInfo.getContainerLocalName("."), enumLeafInfoSchema);
                });


//        Iterator i$ = this.mapping.getEnumLeafInfos().iterator();
//
//        while(i$.hasNext()) {
//            MEnumLeafInfo<T, C> enumLeafInfo = (MEnumLeafInfo)i$.next();
//            EnumLeafInfoProducer<T, C> enumLeafInfoCompiler = new EnumLeafInfoProducer(enumLeafInfo);
//            JsonSchemaBuilder enumLeafInfoSchema = enumLeafInfoCompiler.compile(this);
//            schema.addDefinition(enumLeafInfo.getContainerLocalName("."), enumLeafInfoSchema);
//        }

    }

    private void addClassInfoSchemas(JsonSchemaBuilder schema) {
        mapping.getClassInfos()
                .stream()
                .sorted(Comparator.comparing(t -> t.getContainerLocalName(".")))
                .forEach(classInfo -> {
                    ClassInfoProducer<T, C> classInfoCompiler = new ClassInfoProducer(classInfo);
                    JsonSchemaBuilder classInfoSchema = classInfoCompiler.compile(this);
                    schema.addDefinition(classInfo.getContainerLocalName("."), classInfoSchema);
                });

//        Iterator i$ = this.mapping.getClassInfos().iterator();
//
//        while(i$.hasNext()) {
//            MClassInfo<T, C> classInfo = (MClassInfo)i$.next();
//            ClassInfoProducer<T, C> classInfoCompiler = new ClassInfoProducer(classInfo);
//            JsonSchemaBuilder classInfoSchema = classInfoCompiler.compile(this);
//            schema.addDefinition(classInfo.getContainerLocalName("."), classInfoSchema);
//        }

    }

    public <M extends MOriginated<O>, O> TypeInfoProducer<T, C> getTypeInfoProducer(M originated, MTypeInfo<T, C> typeInfo) {
        return (TypeInfoProducer) typeInfo.acceptTypeInfoVisitor(new CreateTypeInfoProducer(originated));
    }
}
