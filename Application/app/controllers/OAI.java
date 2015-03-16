package controllers;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;


import gr.ntua.ivml.mint.oai.schema.DeletedRecordType;
import gr.ntua.ivml.mint.oai.schema.GetRecordType;
import gr.ntua.ivml.mint.oai.schema.GranularityType;
import gr.ntua.ivml.mint.oai.schema.HeaderType;
import gr.ntua.ivml.mint.oai.schema.IdentifyType;
import gr.ntua.ivml.mint.oai.schema.ListIdentifiersType;
import gr.ntua.ivml.mint.oai.schema.ListMetadataFormatsType;
import gr.ntua.ivml.mint.oai.schema.ListRecordsType;
import gr.ntua.ivml.mint.oai.schema.ListSetsType;
import gr.ntua.ivml.mint.oai.schema.MetadataFormatType;
import gr.ntua.ivml.mint.oai.schema.MetadataType;
import gr.ntua.ivml.mint.oai.schema.OAIPMHerrorType;
import gr.ntua.ivml.mint.oai.schema.OAIPMHerrorcodeType;
import gr.ntua.ivml.mint.oai.schema.OAIPMHtype;
import gr.ntua.ivml.mint.oai.schema.ObjectFactory;
import gr.ntua.ivml.mint.oai.schema.RecordType;
import gr.ntua.ivml.mint.oai.schema.RequestType;
import gr.ntua.ivml.mint.oai.schema.ResumptionTokenType;
import gr.ntua.ivml.mint.oai.schema.SetType;
import gr.ntua.ivml.mint.oai.schema.VerbType;
import gr.ntua.ivml.mint.oai.util.Config;
import gr.ntua.ivml.mint.oai.util.MongoDB;
import play.mvc.*;

import views.html.*;

public class OAI extends Controller {

	private static ObjectFactory fact = new ObjectFactory();
	
	@BodyParser.Of(BodyParser.Xml.class)
	public static Result show(String collection){
		String res = null;
		if(request().queryString().get("verb") != null){
			if(request().queryString().get("verb").length > 0){
				if(request().queryString().get("verb")[0].equals("Identify")){
					JAXBElement elem = getIdentity(collection);
					res = getXML(elem);
				}else if(request().queryString().get("verb")[0].equals("ListRecords")){
					if(request().queryString().get("resumptionToken") != null){
						JAXBElement elem = listRecords(collection, request().queryString().get("resumptionToken")[0]);
						res = getXML(elem);
					}else{
						String prefix = request().queryString().get("metadataPrefix")[0];
						String set = null;
						String from = null;
						String until = null;
						if(request().queryString().get("set") != null){
							set = request().queryString().get("set")[0];
						}
						if(request().queryString().get("from") != null){
							from = request().queryString().get("from")[0];
						}
						if(request().queryString().get("until") != null){
							until = request().queryString().get("until")[0];
						}
						JAXBElement elem = listRecords(collection, from, until, set, prefix, 0);
						res = getXML(elem);
					}
				}else if(request().queryString().get("verb")[0].equals("ListSets")){
					JAXBElement elem = getSets(collection);
					res = getXML(elem);
				}else if(request().queryString().get("verb")[0].equals("ListMetadataFormats")){
					String identifier = null;
					if(request().queryString().get("identifier") != null){
						identifier = request().queryString().get("identifier")[0];
					}
					
					JAXBElement elem = getFormats(collection, identifier);
					res = getXML(elem);
				}else if(request().queryString().get("verb")[0].equals("GetRecord")){
					String metadataPrefix = null;
					String identifier = null;
					
					if(request().queryString().get("metadataPrefix") != null){
						metadataPrefix = request().queryString().get("metadataPrefix")[0];
					}
					if(request().queryString().get("identifier") != null){
						identifier = request().queryString().get("identifier")[0];
					}
					
					JAXBElement elem = getRecord(collection, metadataPrefix, identifier);
					res = getXML(elem);
				}else if(request().queryString().get("verb")[0].equals("ListIdentifiers")){
					if(request().queryString().get("resumptionToken") != null){
						JAXBElement elem = listIdentifiers(collection, request().queryString().get("resumptionToken")[0]);
						res = getXML(elem);
					}else{
						String prefix = request().queryString().get("metadataPrefix")[0];
						String set = null;
						String from = null;
						String until = null;
						if(request().queryString().get("set") != null){
							set = request().queryString().get("set")[0];
						}
						if(request().queryString().get("from") != null){
							from = request().queryString().get("from")[0];
						}
						if(request().queryString().get("until") != null){
							until = request().queryString().get("until")[0];
						}
						JAXBElement elem = listIdentifiers(collection, from, until, set, prefix, 0);
						res = getXML(elem);
					}
				}
			}
		}
		response().setContentType("text/xml");
		
		return ok(res);
	}
	
	
	private static JAXBElement getRecord(String collectionName, String prefix, String identifier){
		JAXBElement res = null;
		
		if(prefix == null){
			return getError(VerbType.GET_RECORD, OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT, null, null, null, prefix, null, collectionName);
		}
		if(identifier == null){
			return getError(VerbType.GET_RECORD, OAIPMHerrorcodeType.BAD_ARGUMENT, null, null, null, prefix, null, collectionName);
		}
		
		BasicDBObject query = new BasicDBObject();
		String[] args = identifier.split(":");
		String hashKey = args[2];
		query.put("hash", hashKey);
		BasicDBObject ns = new BasicDBObject();
		query.put("namespace.prefix", prefix);
		BasicDBObject resQ = (BasicDBObject) MongoDB.getDB().getCollection(collectionName).findOne(query);
		
		if(resQ == null){
			return getError(VerbType.GET_RECORD, OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, null, null, null, prefix, null, collectionName);
		}
		
		GetRecordType rec = fact.createGetRecordType();
		RecordType record = fact.createRecordType();
		MetadataType meta = fact.createMetadataType();
		
		HeaderType header = fact.createHeaderType();
		header.setIdentifier(identifier);
		header.setDatestamp(milisToDate(resQ.getLong("datestamp")));
		record.setHeader(header);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		
		InputSource is = null;
		is = new InputSource( new StringReader( resQ.getString("xmlRecord") ) );
		try {
			doc = builder.parse( is );
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		meta.setAny(doc.getFirstChild());
		record.setMetadata(meta);
		
		rec.setRecord(record);
		
		OAIPMHtype oai = fact.createOAIPMHtype();
		oai.setGetRecord(rec);
		
		try {
			oai.setResponseDate(getCurrentDate());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		RequestType req = fact.createRequestType();
		req.setVerb(VerbType.GET_RECORD);
		req.setValue(Config.get("oai.baseURL")+collectionName+"/oai");
		oai.setRequest(req);
		res = fact.createOAIPMH(oai);
		return res;
	}
	
	private static JAXBElement getFormats(String collectionName, String identifier){
		JAXBElement res = null;
		ArrayList<BasicDBObject> objs = (ArrayList<BasicDBObject>) MongoDB.getDB().getCollection(collectionName).distinct("namespace");
		
		ListMetadataFormatsType forms = fact.createListMetadataFormatsType();
		if(identifier == null){
			for(BasicDBObject obj : objs){
				String prefix = obj.getString("prefix");
				String uri = obj.getString("uri");
				MetadataFormatType type = fact.createMetadataFormatType();
				type.setMetadataPrefix(prefix);
				type.setMetadataNamespace(uri);
				type.setSchema(uri);
				
				forms.getMetadataFormat().add(type);
			}
		}else{
			String[] args = identifier.split(":");
			String id = args[2];
			BasicDBObject resQ = new BasicDBObject();
			resQ.put("hash", id);
			BasicDBObject obj = (BasicDBObject) MongoDB.getDB().getCollection(collectionName).findOne(resQ);
			if(obj == null){
				return  getError(VerbType.LIST_METADATA_FORMATS, OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, null, null, null, null, null, collectionName);
			}else{
				BasicDBObject ns = (BasicDBObject) obj.get("namespace");
				MetadataFormatType type = fact.createMetadataFormatType();
				type.setMetadataPrefix(ns.getString("prefix"));
				type.setMetadataNamespace(ns.getString("uri"));
				type.setSchema(ns.getString("uri"));
				
				forms.getMetadataFormat().add(type);
			}
		}
		
		
		OAIPMHtype oai = fact.createOAIPMHtype();
		oai.setListMetadataFormats(forms);
		
		try {
			oai.setResponseDate(getCurrentDate());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		RequestType req = fact.createRequestType();
		req.setVerb(VerbType.LIST_METADATA_FORMATS);
		req.setValue(Config.get("oai.baseURL")+collectionName+"/oai");
		oai.setRequest(req);
		res = fact.createOAIPMH(oai);
		return res;
	}
	
	
	private static JAXBElement getIdentity(String collectionName){
		IdentifyType ident = fact.createIdentifyType();
		ident.setBaseURL(Config.get("oai.baseURL")+collectionName+"/oai");
		ident.setEarliestDatestamp(Config.get("oai.earliestDate"));
		ident.setDeletedRecord(DeletedRecordType.NO);
		ident.setGranularity(GranularityType.YYYY_MM_DD);
		ident.setProtocolVersion("2.0");
		ident.setRepositoryName(Config.get("oai.repositoryName") + " for project "+ collectionName);
		ident.getAdminEmail().add(Config.get("oai.adminEmail"));
		OAIPMHtype oai = fact.createOAIPMHtype();
		oai.setIdentify(ident);
		try {
			oai.setResponseDate(getCurrentDate());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		RequestType req = fact.createRequestType();
		req.setVerb(VerbType.IDENTIFY);
		req.setValue(Config.get("oai.baseURL")+collectionName+"/oai");
		oai.setRequest(req);
		return fact.createOAIPMH(oai);
	}
	
	
	private static JAXBElement getSets(String collectionName){
		ArrayList<Integer> sets = (ArrayList<Integer>) MongoDB.getDB().getCollection(collectionName).distinct("orgId");
		JAXBElement res = null;
		
		ListSetsType lists = fact.createListSetsType();
		for(Integer in: sets){
			SetType set = fact.createSetType();
			String val = Integer.toString(in);
			set.setSetName(val);
			set.setSetSpec(val);
			lists.getSet().add(set);
		}
		
		OAIPMHtype oai = fact.createOAIPMHtype();
		oai.setListSets(lists);
		
		try {
			oai.setResponseDate(getCurrentDate());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		RequestType req = fact.createRequestType();
		req.setVerb(VerbType.LIST_SETS);
		req.setValue(Config.get("oai.baseURL")+collectionName+"/oai");
		oai.setRequest(req);
		res = fact.createOAIPMH(oai);
		return res;
	}
	
	private static JAXBElement listRecords(String collectionName, String from, String until, String set, String prefix, int index){
		int size = Config.getInt("oai.resultSize");
		JAXBElement res = null;
		OAIPMHerrorType err = null;
		BasicDBObject q = new BasicDBObject();
		if(prefix == null){
			return getError(VerbType.LIST_RECORDS, OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT, from, until, set, prefix, null, collectionName);
		}
		q.put("isPublished", true);
		//BasicDBObject ns = new BasicDBObject();
		//ns.put("prefix",prefix);
		q.put("namespace.prefix", prefix);
		
		if(from != null && until != null){
			long tmpVal = stringDateToMillis(from);
			BasicDBObject range = new BasicDBObject();
			range.put("$gt", tmpVal);
			tmpVal = stringDateToMillis(until);
			range.put("$lt", tmpVal);
			q.put("datestamp", range);
		}else if(from != null && until == null){
			long tmpVal = stringDateToMillis(from);
			BasicDBObject range = new BasicDBObject();
			range.put("$gt", tmpVal);
			q.put("datestamp", range);
		}else if(from == null & until != null){
			long tmpVal = stringDateToMillis(until);
			BasicDBObject range = new BasicDBObject();
			range.put("$lt", tmpVal);
			q.put("datestamp", range);
		}
		
		if(set != null){
			q.put("orgId", Integer.parseInt(set));
		}
		
		int count = MongoDB.getDB().getCollection(collectionName).find(q).limit(size).count();
		if(count == 0){
			return getError(VerbType.LIST_RECORDS, OAIPMHerrorcodeType.NO_RECORDS_MATCH, from, until, set, prefix, null, collectionName);
		}
		DBCursor cur = MongoDB.getDB().getCollection(collectionName).find(q).limit(size).skip(index);

		ListRecordsType list = fact.createListRecordsType();
		
		MetadataType t = null;
		RecordType r = null;
		HeaderType h = null;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		InputSource is = null;
		int countRecs = 0;
		while(cur.hasNext()){
			BasicDBObject obj = (BasicDBObject) cur.next();
			t = fact.createMetadataType();
			h = fact.createHeaderType();
			h.setIdentifier("oai:"+collectionName+":"+obj.getString("hash"));
			h.setDatestamp(milisToDate(obj.getLong("datestamp")));
			is = new InputSource( new StringReader( obj.getString("xmlRecord") ) );
			try {
				doc = builder.parse( is );
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			t.setAny(doc.getFirstChild());
			r = fact.createRecordType();
			r.setHeader(h);
			r.setMetadata(t);
			list.getRecord().add(r);
			countRecs++;
		}
		
		int number = MongoDB.getDB().getCollection(collectionName).find(q).count();
		if(number > index + countRecs){
			ResumptionTokenType resum = fact.createResumptionTokenType();
			resum.setCompleteListSize(BigInteger.valueOf(number));
			resum.setCursor(BigInteger.valueOf(index + countRecs));
			resum.setValue(createResumptionToken(from, until, set, prefix, index + countRecs));
			list.setResumptionToken(resum);
		}
		OAIPMHtype oai = fact.createOAIPMHtype();
		oai.setListRecords(list);
		//System.out.println("size:"+list.getRecord().size());
		try {
			oai.setResponseDate(getCurrentDate());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		RequestType req = fact.createRequestType();
		req.setVerb(VerbType.LIST_RECORDS);
		req.setValue(Config.get("oai.baseURL")+collectionName+"/oai");
		oai.setRequest(req);
		res = fact.createOAIPMH(oai);
		return res;
	}
	
	
	private static JAXBElement listRecords(String collection, String resumptionToken){
		String[] vals = resumptionToken.split("!");
		String from = null;
		String until = null;
		String set = null;
		String prefix = null;
		int cursor = 0;
		if(!vals[0].equals(".")){
			from = milisToDate(Long.parseLong(vals[0]));
		}
		
		if(!vals[1].equals(".")){
			until = milisToDate(Long.parseLong(vals[1]));
		}
		
		if(!vals[2].equals(".")){
			set = vals[2];
		}
		
		prefix = vals[4];
		cursor = Integer.parseInt(vals[3]);
		return listRecords(collection, from, until, set, prefix, cursor);
	}
	
	private static long stringDateToMillis(String date){
		long res = 0;
		 SimpleDateFormat formatter = new SimpleDateFormat(Config.get("oai.granularity"));
		 try {
			Date dat = formatter.parse(date);
			res = dat.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private static String createResumptionToken(String from, String until, String set, String prefix, int cursor){
		String res = "";
		if(from != null){
			res += stringDateToMillis(from) + "!";
		}else{
			res += ".!";
		}
		if(until != null){
			res += stringDateToMillis(until) +"!";
		}else{
			res += ".!";
		}
		if(set != null){
			res += set + "!";
		}else{
			res += ".!";
		}
		res += cursor + "!";
		res += prefix;
		return res;
	}
	
	
	private static String milisToDate(long milis){
		String res = null;
		SimpleDateFormat formatter = new SimpleDateFormat(Config.get("oai.granularity"));
		Calendar cal = Calendar.getInstance();
		
		cal.setTimeInMillis(milis);
		
		java.util.Date date = cal.getTime();
		res = formatter.format(date);
		return res;
	}
	
	private static JAXBElement getError(VerbType verb, OAIPMHerrorcodeType code, String from, String until, String set, String prefix, String resumption, String collectionName){
		JAXBElement res = null;
		OAIPMHerrorType err = fact.createOAIPMHerrorType();
		err.setCode(code);
		if(code == OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT){
			err.setValue("prefix not found");
		}else if(code == OAIPMHerrorcodeType.ID_DOES_NOT_EXIST){
			err.setValue("Record was not found");
		}else if(code == OAIPMHerrorcodeType.NO_RECORDS_MATCH){
			err.setValue("No Records found with the specified arguments");
		}else if(code == OAIPMHerrorcodeType.NO_SET_HIERARCHY){
			err.setValue("Set was not found");
		}
		OAIPMHtype oai = fact.createOAIPMHtype();
		oai.getError().add(err);
		try {
			oai.setResponseDate(getCurrentDate());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		RequestType req = fact.createRequestType();
		req.setVerb(verb);
		if(from != null){
			req.setFrom(from);
		}
		if(until != null){
			req.setUntil(until);
		}
		if(set != null){
			req.setSet(set);
		}
		if(resumption != null){
			req.setResumptionToken(resumption);
		}
		if(prefix != null){
			req.setMetadataPrefix(prefix);
		}
		req.setValue(Config.get("oai.baseURL")+collectionName+"/oai");
		res = fact.createOAIPMH(oai);
		return res;
	}
	
	
	private static String getXML(JAXBElement elem){
		String res = null;
		Class clazz = elem.getValue().getClass();
	    try {
			JAXBContext context =
			    JAXBContext.newInstance( clazz.getPackage().getName() );
			 Marshaller m = context.createMarshaller();
			 m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
			 m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
			 StringWriter writ = new StringWriter();
			 m.marshal(elem, writ);
			 res = writ.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private static XMLGregorianCalendar getCurrentDate() throws DatatypeConfigurationException{
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		return date2;
	}
	
	
	private static JAXBElement listIdentifiers(String collectionName, String from, String until, String set, String prefix, int index){
		int size = Config.getInt("oai.resultSize");
		JAXBElement res = null;
		OAIPMHerrorType err = null;
		BasicDBObject q = new BasicDBObject();
		if(prefix == null){
			return getError(VerbType.LIST_RECORDS, OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT, from, until, set, prefix, null, collectionName);
		}
		q.put("isPublished", true);
		//BasicDBObject ns = new BasicDBObject();
		//ns.put("prefix",prefix);
		q.put("namespace.prefix", prefix);
		
		if(from != null && until != null){
			long tmpVal = stringDateToMillis(from);
			//System.out.println("from:"+tmpVal + " "+from);
			BasicDBObject range = new BasicDBObject();
			range.put("$gt", tmpVal);
			tmpVal = stringDateToMillis(until);
			//System.out.println("until:"+tmpVal + " "+until);
			range.put("$lt", tmpVal);
			q.put("datestamp", range);
		}else if(from != null && until == null){
			long tmpVal = stringDateToMillis(from);
			BasicDBObject range = new BasicDBObject();
			range.put("$gt", tmpVal);
			q.put("datestamp", range);
		}else if(from == null & until != null){
			long tmpVal = stringDateToMillis(until);
			BasicDBObject range = new BasicDBObject();
			range.put("$lt", tmpVal);
			q.put("datestamp", range);
		}
		
		if(set != null){
			q.put("orgId", Integer.parseInt(set));
		}
		//System.out.println(q);
		int count = MongoDB.getDB().getCollection(collectionName).find(q).limit(size).count();
		if(count == 0){
			return getError(VerbType.LIST_RECORDS, OAIPMHerrorcodeType.NO_RECORDS_MATCH, from, until, set, prefix, null, collectionName);
		}
		DBCursor cur = MongoDB.getDB().getCollection(collectionName).find(q).limit(size).skip(index);

		ListIdentifiersType list = fact.createListIdentifiersType();
		
		//MetadataType t = null;
		//RecordType r = null;
		HeaderType h = null;
		
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = null;
//		Document doc = null;
//		
//		try {
//			builder = factory.newDocumentBuilder();
//		} catch (ParserConfigurationException e1) {
//			e1.printStackTrace();
//		}
//		InputSource is = null;
		int countRecs = 0;
		while(cur.hasNext()){
			BasicDBObject obj = (BasicDBObject) cur.next();
			//t = fact.createMetadataType();
			h = fact.createHeaderType();
			h.setIdentifier("oai:"+collectionName+":"+obj.getString("hash"));
			h.setDatestamp(milisToDate(obj.getLong("datestamp")));
//			is = new InputSource( new StringReader( obj.getString("xmlRecord") ) );
//			try {
//				doc = builder.parse( is );
//			} catch (SAXException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			
//			t.setAny(doc.getFirstChild());
//			r = fact.createRecordType();
//			r.setHeader(h);
//			r.setMetadata(t);
			list.getHeader().add(h);
			countRecs++;
		}
		
		int number = MongoDB.getDB().getCollection(collectionName).find(q).count();
		if(number > index + countRecs){
			ResumptionTokenType resum = fact.createResumptionTokenType();
			resum.setCompleteListSize(BigInteger.valueOf(number));
			resum.setCursor(BigInteger.valueOf(index + countRecs));
			resum.setValue(createResumptionToken(from, until, set, prefix, index + countRecs));
			list.setResumptionToken(resum);
		}
		OAIPMHtype oai = fact.createOAIPMHtype();
		oai.setListIdentifiers(list);
		try {
			oai.setResponseDate(getCurrentDate());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		RequestType req = fact.createRequestType();
		req.setVerb(VerbType.LIST_IDENTIFIERS);
		req.setValue(Config.get("oai.baseURL")+collectionName+"/oai");
		oai.setRequest(req);
		res = fact.createOAIPMH(oai);
		return res;
	}
	
	
	private static JAXBElement listIdentifiers(String collection, String resumptionToken){
		String[] vals = resumptionToken.split("!");
		String from = null;
		String until = null;
		String set = null;
		String prefix = null;
		int cursor = 0;
		if(!vals[0].equals(".")){
			from = milisToDate(Long.parseLong(vals[0]));
		}
		
		if(!vals[1].equals(".")){
			until = milisToDate(Long.parseLong(vals[1]));
		}
		
		if(!vals[2].equals(".")){
			set = vals[2];
		}
		
		prefix = vals[4];
		cursor = Integer.parseInt(vals[3]);
		return listIdentifiers(collection, from, until, set, prefix, cursor);
	}
	
}
