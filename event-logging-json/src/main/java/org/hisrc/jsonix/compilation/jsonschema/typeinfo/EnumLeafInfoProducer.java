//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.hisrc.jsonix.compilation.jsonschema.typeinfo;

import com.sun.tools.xjc.model.CClassInfo;
import com.sun.xml.xsom.XmlString;
import java.util.Iterator;
import javax.json.JsonValue;
import javax.xml.namespace.QName;
import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.compilation.jsonschema.JsonSchemaMappingCompiler;
import org.hisrc.jsonix.compilation.jsonschema.JsonixJsonSchemaConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumConstantInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MEnumLeafInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPackagedTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;

public class EnumLeafInfoProducer<T, C extends T> extends PackagedTypeInfoProducer<T, C> {
    private MEnumLeafInfo<T, C> enumLeafInfo;

    public EnumLeafInfoProducer(MEnumLeafInfo<T, C> enumLeafInfo) {
        super((MPackagedTypeInfo)Validate.notNull(enumLeafInfo));
        this.enumLeafInfo = enumLeafInfo;
    }

    public JsonSchemaBuilder compile(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
        JsonSchemaBuilder enumLeafInfoSchema = new JsonSchemaBuilder();
        String localName = this.enumLeafInfo.getContainerLocalName(".");
        enumLeafInfoSchema.addTitle(localName);

        if (this.enumLeafInfo.getTargetType() instanceof CClassInfo) {
            final CClassInfo cClassInfo = (CClassInfo) this.enumLeafInfo.getTargetType();
            if (cClassInfo.javadoc != null) {
                enumLeafInfoSchema.addDescription(cClassInfo.javadoc);
            }
        }

        MTypeInfo<T, C> baseTypeInfo = this.enumLeafInfo.getBaseTypeInfo();
        TypeInfoProducer<T, C> baseTypeInfoProducer = mappingCompiler.getTypeInfoProducer(this.enumLeafInfo, baseTypeInfo);
        JsonSchemaBuilder baseTypeInfoSchema = baseTypeInfoProducer.createTypeInfoSchemaRef(mappingCompiler);
        JsonSchemaBuilder typeInfoSchema = new JsonSchemaBuilder();
        typeInfoSchema.addAllOf(baseTypeInfoSchema);
        JsonSchemaBuilder enumsTypeInfoSchema = new JsonSchemaBuilder();
        boolean valuesSupported = true;
        Iterator i$ = this.enumLeafInfo.getConstants().iterator();

        while(i$.hasNext()) {
            MEnumConstantInfo<T, C> enumConstantInfo = (MEnumConstantInfo)i$.next();
            JsonValue value = baseTypeInfoProducer.createValue(mappingCompiler, enumConstantInfo.getLexicalValue());
            if (value == null) {
                valuesSupported = false;
                break;
            }

            enumsTypeInfoSchema.addEnum(value);
        }

        if (valuesSupported) {
            typeInfoSchema.addAllOf(enumsTypeInfoSchema);
        }

        typeInfoSchema.add(JsonixJsonSchemaConstants.TYPE_TYPE_PROPERTY_NAME, "enumInfo");
        QName typeName = this.enumLeafInfo.getTypeName();
        if (typeName != null) {
            typeInfoSchema.add(JsonixJsonSchemaConstants.TYPE_NAME_PROPERTY_NAME, (new JsonSchemaBuilder()).add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, typeName.getLocalPart()).add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, typeName.getNamespaceURI()));
        }

        return typeInfoSchema;
    }

    public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, String item) {
        MTypeInfo<T, C> baseTypeInfo = this.enumLeafInfo.getBaseTypeInfo();
        TypeInfoProducer<T, C> baseTypeInfoProducer = mappingCompiler.getTypeInfoProducer(this.enumLeafInfo, baseTypeInfo);
        return baseTypeInfoProducer.createValue(mappingCompiler, item);
    }

    public JsonValue createValue(JsonSchemaMappingCompiler<T, C> mappingCompiler, XmlString item) {
        MTypeInfo<T, C> baseTypeInfo = this.enumLeafInfo.getBaseTypeInfo();
        TypeInfoProducer<T, C> baseTypeInfoProducer = mappingCompiler.getTypeInfoProducer(this.enumLeafInfo, baseTypeInfo);
        return baseTypeInfoProducer.createValue(mappingCompiler, item);
    }
}
