package com.mdata.thirdparty.dianli.frontend.forecast;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bileiqi.uncommon", ignoreUnknownFields = true)
public class BileiqiUnCommonConfigBean {
    private Dl dl=new Dl();
    private WD wd=new WD();
    private LJ lj =new LJ();

    public Dl getDl() {
        return dl;
    }

    public void setDl(Dl dl) {
        this.dl = dl;
    }

    public WD getWd() {
        return wd;
    }

    public void setWd(WD wd) {
        this.wd = wd;
    }

    public LJ getLj() {
        return lj;
    }

    public void setLj(LJ lj) {
        this.lj = lj;
    }

    class Dl{
        private long delay;
        private TimeUnit.Unit unit;
        private Double percent;

        public void setPercent(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }


        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        public TimeUnit.Unit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit.Unit unit) {
            this.unit = unit;
        }
    }
    class WD{
        private long delay;
        private TimeUnit.Unit unit;
        private Double percent;

        public void setPercent(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        public TimeUnit.Unit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit.Unit unit) {
            this.unit = unit;
        }
    }
    class LJ{
        private long count;
        private long delay;
        private TimeUnit.Unit unit;
        public long getCount() {
            return count;
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        public TimeUnit.Unit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit.Unit unit) {
            this.unit = unit;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
}
