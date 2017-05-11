package tr.org.liderahenk.disklimit.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.disklimit.constants.disk-limitConstants;
import tr.org.liderahenk.disklimit.dialogs.disk-limitProfileDialog;
import tr.org.liderahenk.disklimit.i18n.Messages;
import tr.org.liderahenk.liderconsole.core.constants.LiderConstants;
import tr.org.liderahenk.liderconsole.core.editorinput.ProfileEditorInput;

/**
 * Profile definition handler for disk-limit plugin.
 *
 */
public class disk-limitProfileHandler extends AbstractHandler {

	private Logger logger = LoggerFactory.getLogger(disk-limitProfileHandler.class);

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        IWorkbenchPage page = window.getActivePage();
        
        try {
			// Here we open default profile editor implementation so that all
			// profiles can be handled by Lider Console Core.
			// We also pass our profile dialog implementation as parameter to
			// allow the editor use it dynamically.
			page.openEditor(new ProfileEditorInput(Messages.getString("disk-limit"), disk-limitConstants.PLUGIN_NAME, 
					disk-limitConstants.PLUGIN_VERSION, new disk-limitProfileDialog()), 
					LiderConstants.EDITORS.PROFILE_EDITOR);
		} catch (PartInitException e) {
			logger.error(e.getMessage(), e);
		}

        return null;
	}

}
