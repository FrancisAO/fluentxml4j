package com.github.fluentxml4j.xpath;

import org.w3c.dom.Document;

public class FluentXPath
{
	public static FromNode from(Document doc)
	{
		return new FromNodeImpl(doc, new FluentXPathContext());
	}
}
