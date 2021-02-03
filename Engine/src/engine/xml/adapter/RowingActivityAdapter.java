package engine.xml.adapter;

import engine.model.activity.rowing.AdaptedRowingActivity;
import engine.model.activity.rowing.RowingActivity;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class RowingActivityAdapter extends XmlAdapter<AdaptedRowingActivity, RowingActivity> {

    @Override
    public RowingActivity unmarshal(AdaptedRowingActivity adaptedRowingActivity) {
        return new RowingActivity(adaptedRowingActivity.getBoat(), adaptedRowingActivity.getRequest());
    }

    @Override
    public AdaptedRowingActivity marshal(RowingActivity rowingActivity)  {
        AdaptedRowingActivity result = new AdaptedRowingActivity();
        result.setBoat(rowingActivity.getBoat());
        result.setRequest(rowingActivity.getRequest());
        return result;
    }
}
