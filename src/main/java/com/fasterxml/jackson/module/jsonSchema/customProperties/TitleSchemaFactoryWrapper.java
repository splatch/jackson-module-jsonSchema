package com.fasterxml.jackson.module.jsonSchema.customProperties;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.ArrayVisitor;
import com.fasterxml.jackson.module.jsonSchema.factories.ObjectVisitor;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.factories.WrapperFactory;

/**
 * Adds a title to every object schema, either root level or nested. Generally
 * useful for writing additional properties to a schema.
 * 
 * @author jphelan
 */
public class TitleSchemaFactoryWrapper extends SchemaFactoryWrapper
{
    private WrapperFactory wrapperFactory = new WrapperFactory() {
	    @Override
	    public SchemaFactoryWrapper getWrapper(SerializerProvider p) {
	        SchemaFactoryWrapper wrapper = new TitleSchemaFactoryWrapper();
	        wrapper.setProvider(p);
	        return wrapper;
	    };
    };

    @Override
    public JsonObjectFormatVisitor expectObjectFormat(JavaType convertedType) {
		ObjectVisitor visitor = ((ObjectVisitor)super.expectObjectFormat(convertedType));
		visitor.setWrapperFactory(wrapperFactory);
		
		// could add other properties here
		addTitle(visitor.getSchema(), convertedType);
		
		return visitor;
    }

    @Override
    public JsonArrayFormatVisitor expectArrayFormat(JavaType convertedType) {
		ArrayVisitor visitor = ((ArrayVisitor)super.expectArrayFormat(convertedType));
		visitor.setWrapperFactory(wrapperFactory);
		
		// could add other properties here
		addTitle(visitor.getSchema(), convertedType);
		
		return visitor;
    }

    /**
     * Adds writes the type as the title of the schema.
     * 
     * @param schema The schema who's title to set.
     * @param type The type of the object represented by the schema.
     */
   private void addTitle(JsonSchema s, JavaType type)
   {
       if (!s.isSimpleTypeSchema()) {
           throw new RuntimeException("given non simple type schema: "+ s.getType());
       }
       s.asSimpleTypeSchema().setTitle(type.getGenericSignature());
   }
}
