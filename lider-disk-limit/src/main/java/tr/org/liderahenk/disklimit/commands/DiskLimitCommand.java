package tr.org.liderahenk.disklimit.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.disklimit.entities.DiskUsageEntity;
import tr.org.liderahenk.lider.core.api.log.IOperationLogService;
import tr.org.liderahenk.lider.core.api.mail.IMailService;
import tr.org.liderahenk.lider.core.api.persistence.IPluginDbService;
import tr.org.liderahenk.lider.core.api.persistence.dao.IPluginDao;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommandExecutionResult;
import tr.org.liderahenk.lider.core.api.plugin.ICommand;
import tr.org.liderahenk.lider.core.api.plugin.IPluginInfo;
import tr.org.liderahenk.lider.core.api.plugin.ITaskAwareCommand;
import tr.org.liderahenk.lider.core.api.service.ICommandContext;
import tr.org.liderahenk.lider.core.api.service.ICommandResult;
import tr.org.liderahenk.lider.core.api.service.ICommandResultFactory;
import tr.org.liderahenk.lider.core.api.service.enums.CommandResultStatus;

public class DiskLimitCommand implements ICommand, ITaskAwareCommand {

	private Logger logger = LoggerFactory.getLogger(DiskLimitCommand.class);

	private ICommandResultFactory resultFactory;
	private IPluginInfo pluginInfo;
	private IOperationLogService logService;
	private IPluginDbService pluginDbService;
	
	private IPluginDao pluginDao;

	private IMailService mailService;

	@Override
	public ICommandResult execute(ICommandContext context) {

		ICommandResult commandResult = resultFactory.create(CommandResultStatus.OK, new ArrayList<String>(), this);

		return commandResult;
	}

	@Override
	public void onTaskUpdate(ICommandExecutionResult result) {

		try {
			Map<String, Object> propertiesMap = new HashMap<String, Object>();
			propertiesMap.put("name", pluginInfo.getPluginName());
			propertiesMap.put("version", pluginInfo.getPluginVersion());


			byte[] data = result.getResponseData();
			
			final Map<String, Object> responseData = new ObjectMapper().readValue(data, 0, data.length,
					new TypeReference<HashMap<String, Object>>() {
					});
			if((Boolean) responseData.get("SaveActive")){
				DiskUsageEntity diskUsageEntity= new DiskUsageEntity();
				diskUsageEntity.setAgentId(result.getAgentId());
				diskUsageEntity.setUsage((Double) responseData.get("usage"));
				diskUsageEntity.setCreateDate(new Date());
				
				pluginDbService.save(diskUsageEntity);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ICommandResult validate(ICommandContext context) {
		return resultFactory.create(CommandResultStatus.OK, null, this, null);
	}

	@Override
	public String getCommandId() {
		return "SET_DISK_LIMIT";
	}

	@Override
	public Boolean executeOnAgent() {
		return true;
	}

	@Override
	public String getPluginName() {
		return pluginInfo.getPluginName();
	}

	@Override
	public String getPluginVersion() {
		return pluginInfo.getPluginVersion();
	}

	public void setResultFactory(ICommandResultFactory resultFactory) {
		this.resultFactory = resultFactory;
	}

	public void setPluginInfo(IPluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
	}

	public void setLogService(IOperationLogService logService) {
		this.logService = logService;
	}

	public void setPluginDbService(IPluginDbService pluginDbService) {
		this.pluginDbService = pluginDbService;
	}

	public IMailService getMailService() {
		return mailService;
	}

	public ICommandResultFactory getResultFactory() {
		return resultFactory;
	}

	public IPluginInfo getPluginInfo() {
		return pluginInfo;
	}

	public IOperationLogService getLogService() {
		return logService;
	}

	public IPluginDbService getPluginDbService() {
		return pluginDbService;
	}

	public IPluginDao getPluginDao() {
		return pluginDao;
	}

	public void setPluginDao(IPluginDao pluginDao) {
		this.pluginDao = pluginDao;
	}


}
