package com.sl.common.dataSource.multids;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "com.sl.datasource")

public class MultiDruidProperties
{
    private List<DruidProperties> multi;

    public List<DruidProperties> getMulti() { return this.multi; }



    public void setMulti(List<DruidProperties> multi) { this.multi = multi; }
}
