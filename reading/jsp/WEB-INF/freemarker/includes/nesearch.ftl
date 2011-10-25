<div id="search" class="widget secondary">
	<div class="header">
	<a id="search-link" href="javascript:toggle('search')" class="toggle">hide</a>
	Named Entity Searches
	</div>
	<div class="contents" id="search-contents">
	<div id="place">
	<form action="nebrowser" method="GET" class="tool" style="width:96.5%; background-color: #F6F6F6">
    <table>
	<tr>
	    <td>Search for places:</td>
	    <td><input name="keyword" size="10" /></td>
	    <td>
		<select name="placetype">
		    <option value="siteName" default="yes">matching this name</option>
		    <option value="state">within this state</option>
		    <option value="nation">within this country</option>
		</select>
	    </td>
	    <td><input type="submit" value="Browse" /></td>
	</tr>
    </table>
    <input type="hidden" name="type" value="place" />
    <p class="description" style="font-size:x-small">Enter the name of a place, like "Springfield" or
    "Athens", to find all locations matching the name, or enter a state
    ("Illinois") or country ("Canada") to find all places within that state or
    nation. You may also enter more than one of these to narrow your search
    ("Athens, Greece" or "Springfield, Illinois, United States").</p>

    <p class="description" style="font-size:x-small">Note that abbreviations ("USA", "Ill.") do
    <em>not</em> work at present--please stick to full names!</p>
</form>
</div>

	<div id="person">
	<form action="nebrowser" method="GET" class="tool" style="width:96.5%; background-color: #F6F6F6">
    <table>
	<tr>
	    <td>Search for a person:</td>
	    <td><input name="keyword" size="30" /></td>
	    <td><input type="submit" value="Browse" /></td>
	</tr>
	<tr>
	    <td style="text-align: right;">In:</td>
	    <td colspan="2">
		<input type="checkbox" name="first">Forenames</input>
		<input type="checkbox" name="last">Surnames</input>
		<input type="checkbox" name="full" checked="checked">Full name</input>
	    </td>
	</tr>
    </table>
    <p class="description" style="font-size:x-small">Searching for "Washington" in "Forenames" and
    "Surnames" will return all people with Washington as a first or last name,
    respectively. A full-name search will find anyone who matches the entire
    search string ("Washington Irving").</p>
    <input type="hidden" name="type" value="person" />
</form>
	</div>
	
	<div id="daterange">
	<form action="nebrowser" method="GET" class="tool" style="width:96.5%; background-color: #F6F6F6">
	<span style="font-size: small;">Search for dates:</span>
    <table>
	<tr>
	    <td>From:</td>
	    <td>
		<select name="month">
		    <option value="">[any]</option>
		    <option value="1">January</option>
		    <option value="2">February</option>
		    <option value="3">March</option>
		    <option value="4">April</option>
		    <option value="5">May</option>
		    <option value="6">June</option>
		    <option value="7">July</option>
		    <option value="8">August</option>
		    <option value="9">September</option>
		    <option value="10">October</option>
		    <option value="11">November</option>
		    <option value="12">December</option>
		</select>
	    </td>
	    <td>
		<input name="day" size="2" />,
	    </td>
	    <td>
		<input name="year" size="4" />
	    </td>
	    <td>
		<select name="era">
		    <option value="AD">A.D.</option>
		    <option value="BC">B.C.</option>
		</select>
	    </td>
	</tr>
	<tr class="date_field_desc">
	    <td></td>
	    <td>Month</td>
	    <td>Day</td>
	    <td>Year</td>
	</tr>
	<tr style="text-align: right;"><td>To:</td>
	    <td>
		<select name="endmonth">
		    <option value="">[any]</option>
		    <option value="1">January</option>
		    <option value="2">February</option>
		    <option value="3">March</option>
		    <option value="4">April</option>
		    <option value="5">May</option>
		    <option value="6">June</option>
		    <option value="7">July</option>
		    <option value="8">August</option>
		    <option value="9">September</option>
		    <option value="10">October</option>
		    <option value="11">November</option>
		    <option value="12">December</option>
		</select>
	    </td>
	    <td>
		<input name="endday" size="2" />,
	    </td>
	    <td>
		<input name="endyear" size="4" />
	    </td>
	    <td>
		<select name="endera">
		    <option value="AD">A.D.</option>
		    <option value="BC">B.C.</option>
		</select>
	    </td>
	    <td><input type="submit" value="Browse" /></td>
	</tr>
    </table>
    <p class="description" style="font-size:x-small">Enter a month, day and/or year to search for references to that date.
    You do not need to fill out every field: searching only for "1863" will
    find all references to the year 1863, while searching for "July 4" will
    find all references to the 4th of July, regardless of year.</p>
    <p class="description" style="font-size:x-small">Enter a starting date and an ending date to find
    all occurrences of dates in between.</p>
    <input type="hidden" name="type" value="date" />
</form>
	</div>

</div>
</div>