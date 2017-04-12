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
package fr.paris.lutece.plugins.myfiles.web.servlet;

import fr.paris.lutece.plugins.myfiles.business.MyFileData;
import fr.paris.lutece.plugins.myfiles.service.storage.FileStorageService;
import fr.paris.lutece.plugins.myfiles.service.storage.NoStorageException;
import fr.paris.lutece.plugins.myfiles.service.storage.StorageException;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * StyleSheetFile Servlet
 */
public class Download extends HttpServlet
{

    public static final int BUFFER_SIZE = 10240;
    private static final String PARAMETER_FILENAME = "filename";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             the servlet Exception
     * @throws IOException
     *             the io exception
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        String strFilename = request.getParameter( PARAMETER_FILENAME );

        if ( user != null && strFilename != null )
        {
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try
            {
                MyFileData myFile = FileStorageService.instance( ).getFile( user.getName( ), strFilename );
                ServletContext context = getServletConfig( ).getServletContext( );
                String strMimetype = context.getMimeType( myFile.getContentType( ) );
                String strSize = Long.toString( myFile.getSize( ) );
                response.setContentType( ( strMimetype != null ) ? strMimetype : "application/octet-stream" );
                response.setHeader( "Content-Length", ( strSize != null ) ? strSize : "" );
                response.setHeader( "Content-Disposition", "attachement; filename=\"" + strFilename + "\"" );

                bis = new BufferedInputStream( myFile.getInputstream( ), BUFFER_SIZE );
                bos = new BufferedOutputStream( response.getOutputStream( ), BUFFER_SIZE );

                byte [ ] buffer = new byte [ BUFFER_SIZE];

                while ( bis.read( buffer ) > 0 )
                {
                    bos.write( buffer );
                }
            }
            catch( NoStorageException ex )
            {
                AppLogService.error( "MyFIles - No storage found : " + ex.getMessage( ), ex );
            }
            catch( StorageException ex )
            {
                AppLogService.error( "MyFIles - Error getting files : " + ex.getMessage( ), ex );
            }
            finally
            {
                if ( bis != null )
                {
                    bis.close( );
                }
                if ( bos != null )
                {
                    bos.close( );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        processRequest( request, response );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo( )
    {
        return "Servlet serving files from bucket";
    }
}
