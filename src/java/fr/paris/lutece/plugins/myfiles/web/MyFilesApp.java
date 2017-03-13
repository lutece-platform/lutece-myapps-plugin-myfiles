/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import fr.paris.lutece.plugins.myfiles.business.MyFileData;
import fr.paris.lutece.plugins.myfiles.business.MyFileLink;
import fr.paris.lutece.plugins.myfiles.service.storage.FileStorageService;
import fr.paris.lutece.plugins.myfiles.service.storage.NoStorageException;
import fr.paris.lutece.plugins.myfiles.service.storage.StorageException;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;

/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "myfiles", pageTitleI18nKey = "myfiles.xpage.myfiles.pageTitle", pagePathI18nKey = "myfiles.xpage.myfiles.pagePathLabel" )
public class MyFilesApp extends MVCApplication
{
    private static final String TEMPLATE_XPAGE = "/skin/plugins/myfiles/myfiles.html";
    private static final String TEMPLATE_CREATE_STORAGE = "/skin/plugins/myfiles/create_storage.html";
    private static final String MARK_FILES_LIST = "files_list";
    private static final String ACTION_CREATE_STORAGE = "createStorage";
    private static final String ACTION_ADD_FILE = "addFile";
    private static final String ACTION_REMOVE_FILE = "removeFile";
    private static final String PARAMETER_FILE = "file";
    private static final String PARAMETER_FILENAME = "filename";
    private static final String VIEW_HOME = "home";

    /**
     * Returns the content of the page myfiles.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     * @throws UserNotSignedException If the user is not signed
     */
    @View( value = VIEW_HOME, defaultView = true )
    public XPage viewHome( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = getUser( request );

        try
        {
            List<MyFileLink> listFiles = FileStorageService.instance( ).getFiles( user.getName( ) );
            Map<String, Object> model = getModel( );

            model.put( MARK_FILES_LIST, listFiles );
            return getXPage( TEMPLATE_XPAGE, request.getLocale( ), model );
        }
        catch( NoStorageException ex )
        {
            return getXPage( TEMPLATE_CREATE_STORAGE, request.getLocale( ) );
        }
        catch( StorageException ex )
        {
            AppLogService.error( "MyFIles - Error getting files : " + ex.getMessage( ), ex );
        }
        return null;
    }

    /**
     * Create a storage for the current user
     * @param request The HTTP request
     * @return XPage
     * @throws UserNotSignedException If the user is not signed 
     */
    @Action( ACTION_CREATE_STORAGE )
    public XPage doCreateStorage( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = getUser( request );
        try
        {
            FileStorageService.instance( ).createStorage( user.getName( ) );
        }
        catch( StorageException ex )
        {
            AppLogService.error( "MyFiles - Error creating storage : " + ex.getMessage( ), ex );
        }

        return redirectView( request, VIEW_HOME );
    }

    /**
     * Add file to the storage of the current user
     * @param request The HTTP request
     * @return XPage
     * @throws UserNotSignedException If the user is not signed 
     */
    @Action( ACTION_ADD_FILE )
    public XPage doAddFile( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = getUser( request );
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        FileItem fileItem = multipartRequest.getFile( PARAMETER_FILE );

        if ( ( fileItem != null ) && ( fileItem.getName( ) != null ) && !"".equals( fileItem.getName( ) ) )
        {
            try
            {
                MyFileData myFileData = new MyFileData( );
                myFileData.setName( fileItem.getName( ) );
                myFileData.setContentType( fileItem.getContentType( ) );
                myFileData.setSize( fileItem.getSize( ) );
                myFileData.setInputstream( new ByteArrayInputStream( fileItem.get( ) ) );
                FileStorageService.instance( ).addFile( user.getName( ), myFileData );
            }
            catch( StorageException ex )
            {
                AppLogService.error( "MyFiles - Error creating storage : " + ex.getMessage( ), ex );
            }

        }

        return redirectView( request, VIEW_HOME );

    }

    /**
     * Remove file from the storage of the current user
     * @param request The HTTP request
     * @return XPage
     * @throws UserNotSignedException If the user is not signed 
     */
    @Action( ACTION_REMOVE_FILE )
    public XPage doRemoveFile( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = getUser( request );
        String strFilename = request.getParameter( PARAMETER_FILENAME );
        try
        {
            FileStorageService.instance( ).removeFile( user.getName( ), strFilename );
        }
        catch( StorageException ex )
        {
            AppLogService.error( "MyFiles - Error removing file : " + ex.getMessage( ), ex );
        }
        return redirectView( request, VIEW_HOME );

    }

    /**
     * Get the current connected user 
     * @param request The HTTP request
     * @return The user
     * @throws UserNotSignedException If the user is not signed
     */
    private LuteceUser getUser( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( user == null )
        {
            throw new UserNotSignedException( );
        }
        return user;

    }

}
