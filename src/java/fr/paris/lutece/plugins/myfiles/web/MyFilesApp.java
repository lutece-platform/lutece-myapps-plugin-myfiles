/*
 * Copyright (c) 2002-2016, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.myfiles.web;

import fr.paris.lutece.plugins.myfiles.service.FileStorageService;
import fr.paris.lutece.plugins.myfiles.service.NoStorageException;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.NoResponseException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import org.xmlpull.v1.XmlPullParserException;

/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "myfiles" , pageTitleI18nKey = "myfiles.xpage.myfiles.pageTitle" , pagePathI18nKey = "myfiles.xpage.myfiles.pagePathLabel" )
public class MyFilesApp extends MVCApplication
{
    private static final String TEMPLATE_XPAGE = "/skin/plugins/myfiles/myfiles.html";
    private static final String TEMPLATE_CREATE_STORAGE = "/skin/plugins/myfiles/create_storage.html";
    private static final String MARK_FILES_LIST = "files_list";
    private static final String ACTION_CREATE_STORAGE = "createStorage";
    private static final String VIEW_HOME = "home";
    
    /**
     * Returns the content of the page myfiles. 
     * @param request The HTTP request
     * @return The view
     * @throws UserNotSignedException
     */
    @View( value = VIEW_HOME , defaultView = true )
    public XPage viewHome( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = getUser( request );
       
        try
        {
            List<String> listFiles = FileStorageService.instance().getFiles( user.getName() );
            Map<String,Object> model = getModel();

            model.put( MARK_FILES_LIST , listFiles );
            return getXPage( TEMPLATE_XPAGE, request.getLocale(  ) , model );
        }
        catch( NoStorageException ex )
        {
            return getXPage( TEMPLATE_CREATE_STORAGE, request.getLocale(  ) );
        }
        catch( InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException | InternalException ex )
        {
            AppLogService.error( "MyFIles - Error getting files : " + ex.getMessage() , ex );
        }
        return null;
    }
    
    @Action( ACTION_CREATE_STORAGE )
    public XPage doCreateStorage( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = getUser( request );
        try
        {
            FileStorageService.instance().createStorage( user.getName() );
        }
        catch( InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException | InternalException ex )
        {
            AppLogService.error( "MyFiles - Error creating storage : " + ex.getMessage() , ex );
        }
        
        return redirectView( request, VIEW_HOME );
    }
    
    private LuteceUser getUser( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = SecurityService.getInstance().getRegisteredUser( request );
        
        if( user == null )
        {
            throw new UserNotSignedException();
        }
        return user;
        
    }

}
