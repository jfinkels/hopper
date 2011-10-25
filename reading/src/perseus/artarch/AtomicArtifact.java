/*
@MPL@
Last Modified: @TIME@

Author: @gweave01@

AtomicArtifact is an interface which consists of member variables found in the
  'atomic' postgres tables.
*/
package perseus.artarch;

public interface AtomicArtifact extends Artifact {

    //getter and setter methods for 'atomic' attributes
    public String getAccessionNumber();
    public void setAccessionNumber(String accessionNumber);

    public String getDimensions();
    public void setDimensions(String dimensions);

    public String getRegion();
    public void setRegion(String region);

    public String getStartDate();
    public void setStartDate(String startDate);

    public String getStartMod();
    public void setStartMod(String startMod);

    public String getEndDate();
    public void setEndDate(String endDate);

    public String getEndMod();
    public void setEndMod(String endMod);

    public String getUnitaryDate();
    public void setUnitaryDate(String unitaryDate);

    public String getUnitaryMod();
    public void setUnitaryMod(String unitaryMod);

    public String getDateForSort();
    public void setDateForSort(String dateForSort);

    public String getPeriod();
    public void setPeriod(String period);

    public String getPeriodForSort();
    public void setPeriodForSort(String periodForSort);

    public String getCulture();
    public void setCulture(String culture);

    public String getContext();
    public void setContext(String context);

    public String getContextMod();
    public void setContextMod(String contextMod);

    public String getFindspot();
    public void setFindspot(String findspot);

    public String getFindspotMod();
    public void setFindspotMod(String findspotMod);

    public String getCollection();
    public void setCollection(String collection);

    public String getDateDescription();
    public void setDateDescription(String dateDescription);

    public String getCollectionHistory();
    public void setCollectionHistory(String collectionHistory);

    public String getDonor();
    public void setDonor(String donor);

    public String getCondition();
    public void setCondition(String condition);

    public String getConditionDescription();
    public void setConditionDescription(String conditionDescription);

    public String getComparanda();
    public void setComparanda(String comparanda);

    public String getMaterial();
    public void setMaterial(String material);

    public String getMaterialDescription();
    public void setMaterialDescription(String materialDescription);

    public String getOtherNotes();
    public void setOtherNotes(String otherNotes);

}
