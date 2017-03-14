package com.github.cbuschka.fluentx;

import org.junit.ClassRule;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.cbuschka.fluentx.FluentXPath.*;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BasicTest
{
	@ClassRule
	public static XmlTestRule docRule = new XmlTestRule("<a><b id=\"_1\">text1 word2</b><b id=\"_2\">text2 word3</b></a>");

	@Test
	public void selectElementsAsNodeList()
	{
		NodeList nodeList = from(docRule.document())
				.selectElements("//b").asNodeList();
		assertThat(nodeList.getLength(), is(2));
	}

	@Test
	public void selectElementsAsListOfElements()
	{
		List<Element> elements = from(docRule.document()).selectElements("//b").asList();
		assertThat(elements.size(), is(2));
		assertThat(elements.get(0).getAttribute("id"), is("_1"));
		assertThat(elements.get(1).getAttribute("id"), is("_2"));
	}

	@Test
	public void selectAttrNodes()
	{
		List<String> values = from(docRule.document())
				.selectNodes("//b/@id")
				.asStream().map((node) -> ((Attr) node).getValue()).collect(Collectors.toList());
		assertThat(values.size(), is(2));
		assertThat(values.get(0), is("_1"));
		assertThat(values.get(1), is("_2"));
	}

	@Test
	public void selectAttributeStrings()
	{
		List<String> values = from(docRule.document())
				.selectStrings("//b/@id").asList();
		assertThat(values.size(), is(2));
		assertThat(values.get(0), is("_1"));
		assertThat(values.get(1), is("_2"));
	}

	@Test
	public void selectSingleElementById()
	{
		Element element = from(docRule.document()).selectElement("//*[@id='_2']").get();
		assertThat(element.getAttribute("id"), is("_2"));
	}

	@Test
	public void selectWords()
	{
		Set<String> words = from(docRule.document())
				.selectStrings("//text()")
				.asStream().flatMap((s) ->
						Arrays.stream(s.split("[\\s\\t]+")))
				.collect(Collectors.toSet());
		assertThat(words, hasItems("text1", "text2", "word2", "word3"));
	}
}
