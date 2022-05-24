//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.hisrc.jsonix.compilation.jsonschema;

import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Multiplicity;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
import com.sun.xml.xsom.XSComponent;
import org.apache.commons.lang3.Validate;
import org.hisrc.jsonix.JsonixConstants;
import org.hisrc.jsonix.jsonschema.JsonSchemaBuilder;
import org.hisrc.jsonix.xml.xsom.ParticleMultiplicityCounter;
import org.hisrc.xml.xsom.XSFunctionApplier;
import org.jvnet.jaxb2_commons.xjc.model.concrete.origin.XJCCMPropertyInfoOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAnyElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MAttributePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElement;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementRefsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeInfos;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementTypeRef;
import org.jvnet.jaxb2_commons.xml.bind.model.MElementsPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MPropertyInfoVisitor;
import org.jvnet.jaxb2_commons.xml.bind.model.MTypeInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MValuePropertyInfo;
import org.jvnet.jaxb2_commons.xml.bind.model.MWrappable;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MElementOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MElementTypeRefOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonSchemaPropertyInfoProducerVisitor<T, C extends T> implements MPropertyInfoVisitor<T, C, JsonSchemaBuilder> {
    private final XSFunctionApplier<Multiplicity> multiplicityCounter;
    private JsonSchemaMappingCompiler<T, C> mappingCompiler;

    public JsonSchemaPropertyInfoProducerVisitor(JsonSchemaMappingCompiler<T, C> mappingCompiler) {
        this.multiplicityCounter = new XSFunctionApplier(ParticleMultiplicityCounter.INSTANCE);
        Validate.notNull(mappingCompiler);
        this.mappingCompiler = mappingCompiler;
    }

    public JsonSchemaBuilder visitElementPropertyInfo(MElementPropertyInfo<T, C> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addPropertyInfoSchema(info, schema);
        this.addPropertyInfoTypeSchema("element", schema);
        this.addElementNameSchema(info.getElementName(), schema);
        this.addWrappableSchema(info, schema);
        JsonSchemaBuilder itemTypeSchema = this.createTypeSchema(info, info.getTypeInfo());
        JsonSchemaBuilder typeSchema = this.createPossiblyCollectionTypeSchema(info, itemTypeSchema);
        schema.addAllOf(typeSchema);
        return schema;
    }

    public JsonSchemaBuilder visitElementsPropertyInfo(MElementsPropertyInfo<T, C> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addPropertyInfoSchema(info, schema);
        this.addPropertyInfoTypeSchema("elements", schema);
        this.addWrappableSchema(info, schema);
        JsonSchemaBuilder itemTypeSchema = this.createElementTypeInfosSchema(info);
        JsonSchemaBuilder typeSchema = this.createPossiblyCollectionTypeSchema(info, itemTypeSchema);
        schema.addAllOf(typeSchema);
        return schema;
    }

    public JsonSchemaBuilder visitElementRefPropertyInfo(MElementRefPropertyInfo<T, C> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addPropertyInfoSchema(info, schema);
        this.addPropertyInfoTypeSchema("elementRef", schema);
        this.addElementNameSchema(info.getElementName(), schema);
        this.addWrappableSchema(info, schema);
        List<JsonSchemaBuilder> itemTypeSchemas = new ArrayList(3);
        if (info.isMixed()) {
            itemTypeSchemas.add((new JsonSchemaBuilder()).addRef(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
        }

        if (info.isDomAllowed()) {
            itemTypeSchemas.add((new JsonSchemaBuilder()).addRef("http://www.jsonix.org/jsonschemas/jsonix/Jsonix.jsonschema#/definitions/dom"));
        }

        if (info.isTypedObjectAllowed()) {
            itemTypeSchemas.add(this.createElementRefSchema(info));
        }

        JsonSchemaBuilder typeSchema = this.createPossiblyCollectionTypeSchema(info, this.createPossiblyAnyOfTypeSchema(itemTypeSchemas));
        schema.addAllOf(typeSchema);
        return schema;
    }

    public JsonSchemaBuilder visitElementRefsPropertyInfo(MElementRefsPropertyInfo<T, C> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addPropertyInfoSchema(info, schema);
        this.addPropertyInfoTypeSchema("elementRefs", schema);
        this.addWrappableSchema(info, schema);
        List<JsonSchemaBuilder> itemTypeSchemas = new ArrayList(2 + info.getElementTypeInfos().size());
        if (info.isMixed()) {
            itemTypeSchemas.add((new JsonSchemaBuilder()).addRef(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
        }

        if (info.isDomAllowed()) {
            itemTypeSchemas.add((new JsonSchemaBuilder()).addRef("http://www.jsonix.org/jsonschemas/jsonix/Jsonix.jsonschema#/definitions/dom"));
        }

        if (info.isTypedObjectAllowed()) {
            itemTypeSchemas.addAll(this.createElementRefsSchema(info));
        }

        JsonSchemaBuilder typeSchema = this.createPossiblyCollectionTypeSchema(info, this.createPossiblyAnyOfTypeSchema(itemTypeSchemas));
        schema.addAllOf(typeSchema);
        return schema;
    }

    public JsonSchemaBuilder visitValuePropertyInfo(MValuePropertyInfo<T, C> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addPropertyInfoSchema(info, schema);
        this.addPropertyInfoTypeSchema("value", schema);
        JsonSchemaBuilder itemTypeSchema = this.createTypeSchema(info, info.getTypeInfo());
        JsonSchemaBuilder typeSchema = this.createPossiblyCollectionTypeSchema(info, itemTypeSchema);
        schema.addAllOf(typeSchema);
        return schema;
    }

    public JsonSchemaBuilder visitAnyElementPropertyInfo(MAnyElementPropertyInfo<T, C> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addPropertyInfoSchema(info, schema);
        this.addPropertyInfoTypeSchema("anyElement", schema);
        List<JsonSchemaBuilder> itemTypeSchemas = new ArrayList(3);
        if (info.isMixed()) {
            itemTypeSchemas.add((new JsonSchemaBuilder()).addRef(XmlSchemaJsonSchemaConstants.STRING_TYPE_INFO_SCHEMA_REF));
        }

        if (info.isDomAllowed()) {
            itemTypeSchemas.add((new JsonSchemaBuilder()).addRef("http://www.jsonix.org/jsonschemas/jsonix/Jsonix.jsonschema#/definitions/dom"));
        }

        JsonSchemaBuilder typeSchema;
        if (info.isTypedObjectAllowed()) {
            typeSchema = (new JsonSchemaBuilder()).addType("object").addProperty(JsonixConstants.NAME_PROPERTY_NAME, (new JsonSchemaBuilder()).addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF)).addProperty(JsonixConstants.VALUE_PROPERTY_NAME, new JsonSchemaBuilder());
            itemTypeSchemas.add(typeSchema);
        }

        typeSchema = this.createPossiblyCollectionTypeSchema(info, this.createPossiblyAnyOfTypeSchema(itemTypeSchemas));
        schema.addAllOf(typeSchema);
        return schema;
    }

    public JsonSchemaBuilder visitAttributePropertyInfo(MAttributePropertyInfo<T, C> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addPropertyInfoSchema(info, schema);
        this.addPropertyInfoTypeSchema("attribute", schema);
        this.addAttributeNameSchema(info.getAttributeName(), schema);
        JsonSchemaBuilder itemTypeSchema = this.createTypeSchema(info, info.getTypeInfo());
        JsonSchemaBuilder typeSchema = this.createPossiblyCollectionTypeSchema(info, itemTypeSchema);
        schema.addAllOf(typeSchema);
        return schema;
    }

    public JsonSchemaBuilder visitAnyAttributePropertyInfo(MAnyAttributePropertyInfo<T, C> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addPropertyInfoSchema(info, schema);
        this.addPropertyInfoTypeSchema("anyAttribute", schema);
        JsonSchemaBuilder typeSchema = (new JsonSchemaBuilder()).addType("object").addAdditionalProperties((new JsonSchemaBuilder()).addType("string"));
        schema.addAllOf(typeSchema);
        return schema;
    }

    private void addPropertyInfoTypeSchema(String string, JsonSchemaBuilder schema) {
        schema.add(JsonixJsonSchemaConstants.PROPERTY_TYPE_PROPERTY_NAME, string);
    }

    private void addPropertyInfoSchema(MPropertyInfo<T, C> propertyInfo, JsonSchemaBuilder schema) {
        schema.addTitle(propertyInfo.getPrivateName());
        final String description = getDescription(propertyInfo);
        if (description != null) {
            schema.addDescription(description);
        }
    }

    private String getDescription(final MPropertyInfo<T, C> info) {
        if (info.getOrigin() instanceof XJCCMPropertyInfoOrigin) {
            final XJCCMPropertyInfoOrigin xjccmPropertyInfoOrigin = (XJCCMPropertyInfoOrigin) info.getOrigin();
            final CPropertyInfo cElementPropertyInfo = xjccmPropertyInfoOrigin.getSource();
            final XSComponent xsComponent = cElementPropertyInfo.getSchemaComponent();
            if (xsComponent != null) {
                if (xsComponent.getAnnotation() != null &&
                        xsComponent.getAnnotation().getAnnotation() != null) {
                    if (xsComponent.getAnnotation().getAnnotation() instanceof BindInfo) {
                        return ((BindInfo) xsComponent.getAnnotation().getAnnotation()).getDocumentation();
                    } else {
                        System.out.println("ink");
                    }
                }
            }
        } else {
            System.out.println("ink");
        }
        return null;
    }

    private void addWrappableSchema(MWrappable info, JsonSchemaBuilder schema) {
        QName wrapperElementName = info.getWrapperElementName();
        if (wrapperElementName != null) {
            this.addNameSchema(schema, JsonixJsonSchemaConstants.WRAPPER_ELEMENT_NAME_PROPERTY_NAME, wrapperElementName);
        }

    }

    private void addNameSchema(JsonSchemaBuilder schema, String key, QName name) {
        schema.add(key, this.createNameSchema(name));
    }

    private JsonSchemaBuilder createNameSchema(QName elementName) {
        return (new JsonSchemaBuilder()).add(JsonixJsonSchemaConstants.LOCAL_PART_PROPERTY_NAME, elementName.getLocalPart()).add(JsonixJsonSchemaConstants.NAMESPACE_URI_PROPERTY_NAME, elementName.getNamespaceURI());
    }

    private void addElementNameSchema(QName elementName, JsonSchemaBuilder schema) {
        this.addNameSchema(schema, JsonixJsonSchemaConstants.ELEMENT_NAME_PROPERTY_NAME, elementName);
    }

    private void addAttributeNameSchema(QName attributeName, JsonSchemaBuilder schema) {
        this.addNameSchema(schema, JsonixJsonSchemaConstants.ATTRIBUTE_NAME_PROPERTY_NAME, attributeName);
    }

    private JsonSchemaBuilder createElementTypeInfosSchema(MElementTypeInfos<T, C, MElementTypeRef<T, C>, MElementTypeRefOrigin> info) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        if (!info.getElementTypeInfos().isEmpty()) {
            Iterator i$ = info.getElementTypeInfos().iterator();

            while (i$.hasNext()) {
                MElementTypeRef<T, C> elementTypeInfo = (MElementTypeRef) i$.next();
                JsonSchemaBuilder elementTypeInfoSchema = this.createElementTypeInfoSchema(elementTypeInfo);
                schema.addAnyOf(elementTypeInfoSchema);
            }
        }

        return schema;
    }

    private JsonSchemaBuilder createElementTypeInfoSchema(MElementTypeRef<T, C> elementTypeInfo) {
        JsonSchemaBuilder elementTypeInfoSchema = new JsonSchemaBuilder();
        this.addElementNameSchema(elementTypeInfo.getElementName(), elementTypeInfoSchema);
        elementTypeInfoSchema.addAnyOf(this.createTypeSchema(elementTypeInfo, elementTypeInfo.getTypeInfo()));
        return elementTypeInfoSchema;
    }

    private List<JsonSchemaBuilder> createElementRefsSchema(MElementTypeInfos<T, C, MElement<T, C>, MElementOrigin> info) {
        List<MElement<T, C>> elementTypeInfos = info.getElementTypeInfos();
        List<JsonSchemaBuilder> schemas = new ArrayList(elementTypeInfos.size());
        Iterator i$ = elementTypeInfos.iterator();

        while (i$.hasNext()) {
            MElement<T, C> elementTypeInfo = (MElement) i$.next();
            JsonSchemaBuilder elementTypeInfoSchema = this.createElementRefSchema(elementTypeInfo);
            schemas.add(elementTypeInfoSchema);
        }

        return schemas;
    }

    private <M extends MElementTypeInfo<T, C, O>, O> JsonSchemaBuilder createElementRefSchema(M elementTypeInfo) {
        JsonSchemaBuilder schema = new JsonSchemaBuilder();
        this.addElementNameSchema(elementTypeInfo.getElementName(), schema);
        schema.addType("object");
        schema.addProperty(JsonixConstants.NAME_PROPERTY_NAME, (new JsonSchemaBuilder()).addRef(XmlSchemaJsonSchemaConstants.QNAME_TYPE_INFO_SCHEMA_REF));
        schema.addProperty(JsonixConstants.VALUE_PROPERTY_NAME, this.createTypeSchema(elementTypeInfo, elementTypeInfo.getTypeInfo()));
        return schema;
    }

    private JsonSchemaBuilder createPossiblyAnyOfTypeSchema(List<JsonSchemaBuilder> schemas) {
        if (schemas.size() == 0) {
            return new JsonSchemaBuilder();
        } else if (schemas.size() == 1) {
            return (JsonSchemaBuilder) schemas.get(0);
        } else {
            JsonSchemaBuilder schema = new JsonSchemaBuilder();
            schema.addAnyOf(schemas);
            return schema;
        }
    }

    private JsonSchemaBuilder createPossiblyCollectionTypeSchema(MPropertyInfo<T, C> propertyInfo, JsonSchemaBuilder itemTypeSchema) {
        JsonSchemaBuilder typeSchema;
        if (propertyInfo.isCollection()) {
            typeSchema = new JsonSchemaBuilder();
            typeSchema.addType("array").addItem(itemTypeSchema);
            Multiplicity multiplicity = (Multiplicity) this.multiplicityCounter.apply(propertyInfo.getOrigin());
            if (multiplicity != null) {
                if (multiplicity.min != null) {
                    typeSchema.addMinItems(multiplicity.min);
                }

                if (multiplicity.max != null) {
                    typeSchema.addMaxItems(multiplicity.max);
                }
            }
        } else {
            typeSchema = itemTypeSchema;
        }

        return typeSchema;
    }

    private <M extends MOriginated<O>, O> JsonSchemaBuilder createTypeSchema(M originated, MTypeInfo<T, C> typeInfo) {
        return (JsonSchemaBuilder) typeInfo.acceptTypeInfoVisitor(new CreateTypeInfoSchema(this.mappingCompiler, originated));
    }
}
