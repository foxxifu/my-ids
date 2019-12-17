package com.interest.ids.gatekeeper.server.parse;

import java.io.File;

import com.interest.ids.gatekeeper.server.utils.GateKeeperConstant;

/**
 * 
 * @author lhq
 *
 *
 */
public class AlarmFileDataParser implements FileDataParser {

	/**
	 * load data [low_priority] [local] infile'file_name txt' [replace | ignore]
	 * into table tbl_name [fields [terminated by't'] [OPTIONALLY] enclosed by
	 * ''] [escaped by'\' ]] [lines terminated by'n'] [ignore number lines]
	 * [(col_name, )]
	 */
	@Override
	public void parse(File file) {

	}

	@Override
	public byte getDataType() {

		return GateKeeperConstant.ALARM_TYPE;
	}

}
