

function expandContract(author, size) {
	var idstring = author + ",0," + size;
	var expander = document.getElementById(idstring);
	var eString = expander.innerHTML.split("(")[0];
	if( eString == "More" ) {
		expand(author, size);
		expander.innerHTML = "Less";
	} else {
		contract(author, size);
		expander.innerHTML = "More(" + size + ")";
	}
}

function expand(author, size)
{
        var trExpand;
        var idString;
        for (i = 0; i < size +1; i++)
        {
		if( i == 1 )continue;
                idString = author + "," + i + ",0";
                trExpand = document.getElementById(idString);
                trExpand.style.display = "";
        }

}

function contract(author, size)
{
        var trExpand;
        var idString;
        for (i = size; i >= 0; i--)
        {
		if( i == 0 )continue;
                idString = author + "," + i + ",0";
                trExpand = document.getElementById(idString);
                trExpand.style.display = "none";
        }

}