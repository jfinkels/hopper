
function expandContract(author, size)
{
	var idstring = author + ",0," + size;
	var hiddenEle= document.getElementById(author+",1,0");
	var expander = document.getElementById(idstring);
	var arrow    = expander.childNodes[0];
	if(hiddenEle.style.display=="none"){
		expander.childNodes[0].src = "/img/south.gif";
		expand(author,size);
	} else {
		contract(author,size);	
		expander.childNodes[0].src = "/img/east.gif";
	}
}
function expand(author,size)
{
	var trExpand;
	var idString;
	for (i = 1; i <= size; i++)
	{
		idString = author + "," + i + ",0";
		trExpand = document.getElementById(idString);
		trExpand.style.display = "";	
	}

}
function contract(author,size)
{
	var trExpand;
	var idString;
	for (i = size; i >= 1; i--)
	{
		idString = author + "," + i + ",0";
		trExpand = document.getElementById(idString);
		trExpand.style.display = "none";	
	}

}
function changeCursor()
{
	document.body.style.cursor = 'pointer';
}
function revertCursor()
{
	document.body.style.cursor = 'auto';
}