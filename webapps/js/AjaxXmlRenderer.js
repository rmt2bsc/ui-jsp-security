/**
 * Renders various XML nodes via XPath using XML as the input source. This
 * library depends on AjaxRequestConfig.js and xpath.js libraries.
 */
function AjaxXmlRenderer(reqConfig, xmlData) {
	var config = reqConfig;
	var data = xmlData;
	var parser, xmlDoc;
	parser = new DOMParser();
	xmlDoc = parser.parseFromString(xmlData, "text/xml");
	
	// Regarding Microsoft browsers, XPathEvaluator is not supported in IE.  Edge is preferred.
 	var xmlEvaluator = new XPathEvaluator();
	var xmlResolver = xmlEvaluator.createNSResolver(xmlDoc); 

	
	/**
	 * Builds the select options for a an input select control based on the XML
	 * data derived from the user's Ajax request.  The property names of the 
	 * "codeXPath" and "textXPath" are expected to be named exactly 
	 * as the target input control property name.
	 */
	this.buildSelectOptions = function(selObj, codeXPath, textXPath) {
		const codes = xmlEvaluator.evaluate(codeXPath, xmlDoc, xmlResolver, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		const text = xmlEvaluator.evaluate(textXPath, xmlDoc, xmlResolver, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);

		// remove all items from the HTML Select component
		this.removeSelectOptions(selObj);
		
		// Update the HTML Select component with new items
		let code = null;
		var ndx = 0;
		const tagNames = [];
		while ((code = codes.iterateNext())) {
			displayText = text.iterateNext()
			selObj.options[ndx] = new Option(displayText.textContent, code.textContent);
			ndx++;
		}
		return;
	}

	/**
	 * Remove all options from the select control in reverse order. It is
	 * required to traverse the list in reverse order since the list is
	 * restructured every time an option is set to null.
	 */
	this.removeSelectOptions = function(selObj) {
		for (ndx = (selObj.length - 1); ndx >= 0; ndx--) {
			selObj.options[ndx] = null;
		}
		if (selObj.length > 0) {
			selObj.selectedIndex = (selIndex == 0 ? 0 : selIndex - 1);
		}
	}

	/**
	 * Created HTML data from the XML derived from the user's Ajax request.
	 */
	this.renderHtml = function() {
		if (!validate()) {
			throw "HTML Builder failed: the component that is to be rendered has an invalid reference";
		}
		// Produce plain text
		if (config.component == null) {
			renderPlainText();
		}
		if (config.component.type == "text") {
			renderText();
		}
		if (config.component.type == "checkbox") {
			renderCheckbox();
		}
		if (config.component.type == "radio") {
			renderRadio();
		}
		if (config.component.type == "textarea") {
			renderTextArea();
		}
		if (config.component.type == "select-one"
				|| config.component.type == "select-multiple") {
			renderSelect();
		}
	}

	var validate = function() {
		if (config == null) {
			return false;
		}
		if (config.component.type != "text"
				&& config.component.type != "checkbox"
				&& config.component.type != "radio"
				&& config.component.type != "textarea"
				&& config.component.type != "select-one"
				&& config.component.type != "select-multiple") {
			return false;
		}
		return true;
	}

	var renderPlainText = function() {
		return getPropertyValue(config.displayValueId);
		;
	}

	var renderText = function() {
		var propVal = getPropertyValue(config.displayValueId);
		config.component.value = propVal;
		return;
	}

	var renderCheckbox = function() {
		return;
	}

	var renderRadio = function() {
		return;
	}

	var renderTextArea = function() {
		return;
	}

	var renderSelect = function() {
		return;
	}

}
