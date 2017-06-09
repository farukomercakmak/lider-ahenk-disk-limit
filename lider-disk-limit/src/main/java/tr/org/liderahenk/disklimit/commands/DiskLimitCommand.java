package tr.org.liderahenk.disklimit.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.lider.core.api.log.IOperationLogService;
import tr.org.liderahenk.lider.core.api.mail.IMailService;
import tr.org.liderahenk.lider.core.api.persistence.IPluginDbService;
import tr.org.liderahenk.lider.core.api.persistence.dao.IMailAddressDao;
import tr.org.liderahenk.lider.core.api.persistence.dao.IPluginDao;
import tr.org.liderahenk.lider.core.api.persistence.entities.ICommandExecutionResult;
import tr.org.liderahenk.lider.core.api.persistence.entities.IMailAddress;
import tr.org.liderahenk.lider.core.api.persistence.entities.IPlugin;
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
	private IMailAddressDao mailAddressDao;

	@Override
	public ICommandResult execute(ICommandContext context) {

		// TODO Modify parameter map before sending it to agent(s).
		// ITaskRequest req = context.getRequest();
		// Map<String, Object> parameterMap = req.getParameterMap();
		// parameterMap.put("dummy-param", "dummy-param-value");
		//
		// logger.debug("Parameter map updated.");
		//
		// // TODO Modify entity objects related to plugin command via DB
		// service
		// //Object entity = new Object();
		// //pluginDbService.save(entity);
		// logger.debug("Entity saved successfully.");
		//
		// // TODO Modify result map to provide additional parameters or info
		// before sending it back to console.
		// Map<String, Object> resultMap = new HashMap<String, Object>();
		// resultMap.put("dummy-param", "dummy-param-value");
		//
		// logger.debug("Executed command, returning result.");
		ICommandResult commandResult = resultFactory.create(CommandResultStatus.OK, new ArrayList<String>(), this);

		return commandResult;
	}

	@Override
	public void onTaskUpdate(ICommandExecutionResult result) {

		try {
			Map<String, Object> propertiesMap = new HashMap<String, Object>();
			propertiesMap.put("name", pluginInfo.getPluginName());
			propertiesMap.put("version", pluginInfo.getPluginVersion());

			List<? extends IPlugin> plugins = pluginDao.findByProperties(IPlugin.class, propertiesMap, null, null);

			byte[] data = result.getResponseData();
			
			final Map<String, Object> responseData = new ObjectMapper().readValue(data, 0, data.length,
					new TypeReference<HashMap<String, Object>>() {
					});

			String resultData= (String) responseData.get("Result");
			String mail_content = (String) responseData.get("mail_content");
			String mail_subject = (String) responseData.get("mail_subject");
			
			responseData.get("");
//			if (plugins != null && plugins.size() > 0) {
//
//				logger.info("plugins : " + plugins.get(0).getName() + " " + plugins.get(0).getVersion());
//
//				if (getMailAddressDao() == null)
//					logger.debug("mail dao " + getMailAddressDao());
//
//				List<? extends IMailAddress> mailAddressList = getMailAddressDao().findByProperty(IMailAddress.class,
//						"plugin.id", plugins.get(0).getId(), 0);
//
//				List<String> toList = new ArrayList<String>();
//				for (IMailAddress iMailAddress : mailAddressList) {
//					toList.add(iMailAddress.getMailAddress());
//				}
//				if(toList.size()>0)
//				getMailService().sendMail(toList, mail_subject, mail_content);
//
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public ICommandResult validate(ICommandContext context) {
		// TODO Validate before command execution
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

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public IMailAddressDao getMailAddressDao() {
		return mailAddressDao;
	}

	public void setMailAddressDao(IMailAddressDao mailAddressDao) {
		this.mailAddressDao = mailAddressDao;
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
