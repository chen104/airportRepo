package com.ubtech.airport.ibm.bean;


import com.ubtech.airport.ibm.utils.SystemPropertiesHelper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class CmdBean {
	private List<Cmds> cmds;

	public void setCmds(List<Cmds> cmds) {
		this.cmds = cmds;
	}

	public List<Cmds> getCmds() {
		return cmds;
	}

	public void refreshProp() {
		Iterator<Cmds> it = cmds.iterator();
		while (it.hasNext()) {
			Cmds c = it.next();
			//此字段和config.json 文件里面的配置name字段匹配，不可随意更改
			if ("DIAITAL".equals(c.getName())) {
				c.setName("true".equals(SystemPropertiesHelper.get(SystemPropertiesHelper.AUDIO_DIGITAL)) ? "当前:数字" : "当前:模拟");
			}
		}
	}

	public static class Cmds {

		private String name;
		private int cid;
		private String tip;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setCid(int cid) {
			this.cid = cid;
		}

		public int getCid() {
			return cid;
		}

		public String getTip() {
			return tip;
		}

		public void setTip(String tip) {
			this.tip = tip;
		}
	}
}
