//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.Multiplicity;
import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaPropertyInfoProducerVisitor;
import org.hisrc.jsonix.compilation.jsonschema.JsonixJsonSchemaConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.xml.xsom.ParticleMultiplicityCounter;
import org.hisrc.xml.xsom.XSFunctionApplier;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MClassTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackagedTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassInfoProducer<T, C extends T> extends PackagedTypeInfoProducer<T, C> {
    private final XSFunctionApplier<Multiplicity> multiplicityCounter;
    private final MClassInfo<T, C> classInfo;

    public ClassInfoProducer(MClassInfo<T, C> classInfo) {
        super((MPackagedTypeInfo) Validate.notNull(classInfo));
        this.multiplicityCounter = new XSFunctionApplier(ParticleMultiplicityCounter.INSTANCE);
        this.classInfo = classInfo;
    }

    public JsonSchemaBuilder compile(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
        JsonSchemaBuilder classInfoSchema = new JsonSchemaBuilder();
        classInfoSchema.addType("object");
        String localName = this.classInfo.getContainerLocalName(".");
        classInfoSchema.addTitle(localName);

        if (this.classInfo.getTargetType() instanceof CClassInfo) {
            final CClassInfo cClassInfo = (CClassInfo) this.classInfo.getTargetType();
            if (cClassInfo.getSchemaComponent() != null &&
                    cClassInfo.getSchemaComponent().getAnnotation() != null &&
                    (cClassInfo.getSchemaComponent().getAnnotation().getAnnotation() instanceof
                            com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo)) {
                final com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo bindInfo =
                        (com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo)
                                cClassInfo.getSchemaComponent().getAnnotation().getAnnotation();
                classInfoSchema.addDescription(bindInfo.getDocumentation());
            } else if (cClassInfo.javadoc != null) {
                classInfoSchema.addDescription(cClassInfo.javadoc);
            }
        }

        MClassTypeInfo<T, C, ?> baseTypeInfo = this.classInfo.getBaseTypeInfo();
        JsonSchemaBuilder typeInfoSchema;
        if (baseTypeInfo != null) {
            JsonSchemaBuilder baseTypeInfoSchema = mappingCompiler.getTypeInfoProducer(baseTypeInfo, baseTypeInfo).createTypeInfoSchemaRef(mappingCompiler);
            typeInfoSchema = new JsonSchemaBuilder();
            typeInfoSchema.addAllOf(baseTypeInfoSchema);
            typeInfoSchema.addAllOf(classInfoSchema);
        } else {
            typeInfoSchema = classInfoSchema;
        }

        Map<String, JsonSchemaBuilder> propertyInfoSchemas = this.compilePropertyInfos(mappingCompiler);
        List<String> propertiesOrder = new ArrayList(propertyInfoSchemas.size());
        propertiesOrder.addAll(propertyInfoSchemas.keySet());
        classInfoSchema.addProperties(propertyInfoSchemas);
        Iterator i$ = this.classInfo.getProperties().iterator();

        while (i$.hasNext()) {
            MPropertyInfo<T, C> propertyInfo = (MPropertyInfo) i$.next();
            Multiplicity multiplicity = (Multiplicity) this.multiplicityCounter.apply(propertyInfo.getOrigin());
            if (multiplicity != null && multiplicity.min != null && multiplicity.min.compareTo(BigInteger.ZERO) > 0) {
                typeInfoSchema.addRequired(propertyInfo.getPrivateName());
            }
        }

        typeInfoSchema.add(JsonixJsonSchemaConstants.TYPE_TYPE_PROPERTY_NAME, "classInfo");
        QName typeName = this.classInfo.getTypeName();
        if (typeName != null) {
            typeInfoSchema.add(JsonixJsonSchemaConstants.TYPE_NAME_PROPERTY_NAME, (new JsonSchemaBuilder()).add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, typeName.getLocalPart()).add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, typeName.getNamespaceURI()));
        }

        if (!propertiesOrder.isEmpty()) {
            typeInfoSchema.add("propertiesOrder", propertiesOrder);
        }

        return typeInfoSchema;
    }

    private Map<String, JsonSchemaBuilder> compilePropertyInfos(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
        Map<String, JsonSchemaBuilder> propertyInfoSchemas = new LinkedHashMap(this.classInfo.getProperties().size());
        Iterator i$ = this.classInfo.getProperties().iterator();

        while (i$.hasNext()) {
            MPropertyInfo<T, C> propertyInfo = (MPropertyInfo) i$.next();
            if (mappingCompiler.getMapping().getPropertyInfos().contains(propertyInfo)) {
                propertyInfoSchemas.put(propertyInfo.getPrivateName(), (JsonSchemaBuilder) propertyInfo.acceptPropertyInfoVisitor(new JsonSchemaPropertyInfoProducerVisitor(mappingCompiler)));
            }
        }

        return propertyInfoSchemas;
    }
}
