package arcadeflex056;

import static common.libc.cstdio.*;
import static common.libc.cstring.*;
import static arcadeflex036.ticker.*;
import static arcadeflex036.config.*;
import static arcadeflex056.fronthlp.*;
import static arcadeflex056.settings.current_platform_configuration;
import arcoflex056.platform.platformConfigurator;
import arcoflex056.platform.platformConfigurator.i_URLDownloadProgress_class;
import static mame056.driver.drivers;
import static mame056.mame.*;
import static mess056.msdos.load_image;

/**
 * This file is relative to msdos.c in mame
 *
 * @author chusogar
 */

public class osdepend {
	
	static FILE errorlog=null;
    public static int ignorecfg;
    public static i_URLDownloadProgress_class dlprogress;
    /* put here anything you need to do when the program is started. Return 0 if */
    /* initialization was successful, nonzero otherwise. */
    static String crcfilename = "";
    static String crcfile = crcfilename;
    static String pcrcfilename = "";
    static String pcrcfile = pcrcfilename;
    
    /*TODO*///	static void signal_handler(int num)
    /*TODO*///	{
    /*TODO*///		if (errorlog) fflush(errorlog);
    /*TODO*///	
    /*TODO*///		osd_exit();
    /*TODO*///		allegro_exit();
    /*TODO*///		ScreenClear();
    /*TODO*///		ScreenSetCursor( 0, 0 );
    /*TODO*///		if( num == SIGINT )
    /*TODO*///			cpu_dump_states();
    /*TODO*///	
    /*TODO*///		signal(num, SIG_DFL);
    /*TODO*///		raise(num);
    /*TODO*///	}
    
    /* put here anything you need to do when the program is started. Return 0 if */
    /* initialization was successful, nonzero otherwise. */
    public static int osd_init()
    {
    /*TODO*///		if (msdos_init_sound())
    /*TODO*///			return 1;
    /*TODO*///		msdos_init_input();
    		return 0;
    }
    
    /* put here cleanup routines to be executed when the program is terminated. */
    public static void osd_exit()
    {
    /*TODO*///		msdos_shutdown_sound();
    /*TODO*///		msdos_shutdown_input();
    }
    	
    /*TODO*///	/* fuzzy string compare, compare short string against long string        */
    /*TODO*///	/* e.g. astdel == "Asteroids Deluxe". The return code is the fuzz index, */
    /*TODO*///	/* we simply count the gaps between maching chars.                       */
    /*TODO*///	int fuzzycmp (const char *s, const char *l)
    /*TODO*///	{
    /*TODO*///		int gaps = 0;
    /*TODO*///		int match = 0;
    /*TODO*///		int last = 1;
    /*TODO*///	
    /*TODO*///		for (; *s && *l; l++)
    /*TODO*///		{
    /*TODO*///			if (*s == *l)
    /*TODO*///				match = 1;
    /*TODO*///			else if (*s >= 'a' && *s <= 'z' && (*s - 'a') == (*l - 'A'))
    /*TODO*///				match = 1;
    /*TODO*///			else if (*s >= 'A' && *s <= 'Z' && (*s - 'A') == (*l - 'a'))
    /*TODO*///				match = 1;
    /*TODO*///			else
    /*TODO*///				match = 0;
    /*TODO*///	
    /*TODO*///			if (match)
    /*TODO*///				s++;
    /*TODO*///	
    /*TODO*///			if (match != last)
    /*TODO*///			{
    /*TODO*///				last = match;
    /*TODO*///				if (match == 0)
    /*TODO*///					gaps++;
    /*TODO*///			}
    /*TODO*///		}
    /*TODO*///	
    /*TODO*///		/* penalty if short string does not completely fit in */
    /*TODO*///		for (; *s; s++)
    /*TODO*///			gaps++;
    /*TODO*///	
    /*TODO*///		return gaps;
    /*TODO*///	}
    
    public static int main (int argc, String[] argv)
    {
	    	dlprogress = current_platform_configuration.get_URLDownloadProgress_class();
	        dlprogress.setVersion(settings.system_name + " version: " + settings.version + " (" + current_platform_configuration.getPlatformName() + " platform)");
	        System.out.println(settings.system_name + " version: " + settings.version + " (" + current_platform_configuration.getPlatformName() + " platform)");
	        dlprogress.setVisible(true);
	        int res=0, i, j = 0, game_index;
	        String playbackname = "";
    /*TODO*///		char override_path[256];
    /*TODO*///	
    /*TODO*///	
    /*TODO*///		override_path[0] = 0;
    /*TODO*///	
    /*TODO*///		memset(&options,0,sizeof(options));
    	
    		/* these two are not available in mame.cfg */
    		ignorecfg = 0;
    		errorlog = null;
    	
    		game_index = -1;
    
	    	for (i = 1;i < argc;i++) /* V.V_121997 */
	    	{
	    		if (stricmp(argv[i],"-ignorecfg") == 0) ignorecfg = 1;
	    		if (stricmp(argv[i],"-log") == 0)
	    			errorlog = fopen("error.log","wa");
	            if (stricmp(argv[i],"-playback") == 0)
	    		{
	    			i++;
	    			if (i < argc)  /* point to inp file name */
	    				playbackname = argv[i];
	            }
	    	}
    
    /*TODO*///	    allegro_init();
    /*TODO*///	
    /*TODO*///		/* Allegro changed the signal handlers... change them again to ours, to */
    /*TODO*///		/* avoid the "Shutting down Allegro" message which confuses users into */
    /*TODO*///		/* thinking crashes are caused by Allegro. */
    /*TODO*///		signal(SIGABRT, signal_handler);
    /*TODO*///		signal(SIGFPE,  signal_handler);
    /*TODO*///		signal(SIGILL,  signal_handler);
    /*TODO*///		signal(SIGSEGV, signal_handler);
    /*TODO*///		signal(SIGTERM, signal_handler);
    /*TODO*///		signal(SIGINT,  signal_handler);
    /*TODO*///		signal(SIGKILL, signal_handler);
    /*TODO*///		signal(SIGQUIT, signal_handler);
    /*TODO*///	
    /*TODO*///		#ifdef MESS
    /*TODO*///	    set_config_file ("mess.cfg");
    /*TODO*///		#else
    /*TODO*///	    set_config_file ("mame.cfg");
    /*TODO*///		#endif
    
	    	/* check for frontend options */
	        res = frontend_help (argc, argv);
	    
	    	/* if frontend options were used, return to DOS with the error code */
	    	if (res != 1234)
	    		return res;
    
    /*TODO*///		/* Initialize the audio library */
    /*TODO*///		if (msdos_init_seal())
    /*TODO*///		{
    /*TODO*///			printf ("Unable to initialize SEAL\n");
    /*TODO*///			return (1);
    /*TODO*///		}
    /*TODO*///	
    		init_ticker();	/* after Allegro init because we use cpu_cpuid */
    /*TODO*///	
    /*TODO*///	    /* handle playback which is not available in mame.cfg */
    /*TODO*///		init_inpdir(); /* Init input directory for opening .inp for playback */
    /*TODO*///	
    /*TODO*///	    if (playbackname != NULL)
    /*TODO*///	        options.playback = osd_fopen(playbackname,0,OSD_FILETYPE_INPUTLOG,0);
    /*TODO*///	
    /*TODO*///	    /* check for game name embedded in .inp header */
    /*TODO*///	    if (options.playback)
    /*TODO*///	    {
    /*TODO*///	        INP_HEADER inp_header;
    /*TODO*///	
    /*TODO*///	        /* read playback header */
    /*TODO*///	        osd_fread(options.playback, &inp_header, sizeof(INP_HEADER));
    /*TODO*///	
    /*TODO*///	        if (!isalnum(inp_header.name[0])) /* If first byte is not alpha-numeric */
    /*TODO*///	            osd_fseek(options.playback, 0, SEEK_SET); /* old .inp file - no header */
    /*TODO*///	        else
    /*TODO*///	        {
    /*TODO*///	            for (i = 0; (drivers[i] != 0); i++) /* find game and play it */
    /*TODO*///				{
    /*TODO*///	                if (strcmp(drivers[i]->name, inp_header.name) == 0)
    /*TODO*///	                {
    /*TODO*///	                    game_index = i;
    /*TODO*///	                    printf("Playing back previously recorded game %s (%s) [press return]\n",
    /*TODO*///	                        drivers[game_index]->name,drivers[game_index]->description);
    /*TODO*///	                    getchar();
    /*TODO*///	                    break;
    /*TODO*///	                }
    /*TODO*///	            }
    /*TODO*///	        }
    /*TODO*///	    }
	
			/* If not playing back a new .inp file */
		    if (game_index == -1)
		    {
		        /* take the first commandline argument without "-" as the game name */
	
	
		    	for (j = 1; j < argc; j++) {
		    		if (!argv[j].startsWith("-")) {
	                    break;//if (argv[j][0] != '-') break; (original c code,conversion seems ok)
	                }
	            }
    			/* do we have a driver for this? */
		    	for (i = 0; drivers[i] != null && (game_index == -1); i++) {
	                if (stricmp(argv[j], drivers[i].name) == 0) {
	                    game_index = i;
	                    dlprogress.setRomName("loading game: "+drivers[i].name);
	                    break;
	                }
	            }
    /*TODO*///	#ifdef MAME_DEBUG
    /*TODO*///	        /* pick a random game */
    /*TODO*///	        if (stricmp(argv[j],"random") == 0)
    /*TODO*///	        {
    /*TODO*///	            struct timeval t;
    /*TODO*///	
    /*TODO*///	            i = 0;
    /*TODO*///	            while (drivers[i]) i++;	/* count available drivers */
    /*TODO*///	
    /*TODO*///	            gettimeofday(&t,0);
    /*TODO*///	            srand(t.tv_sec);
    /*TODO*///	            game_index = rand() % i;
    /*TODO*///	
    /*TODO*///	            printf("Running %s (%s) [press return]\n",drivers[game_index]->name,drivers[game_index]->description);
    /*TODO*///	            getchar();
    /*TODO*///	        }
    /*TODO*///	        else
    /*TODO*///	#endif
    	        {
    /*TODO*///				char gamename[256];
    /*TODO*///				char *n,*c;
    /*TODO*///	
    /*TODO*///	
    /*TODO*///				/* separate leading path */
    /*TODO*///				strcpy(override_path,argv[j]);
    /*TODO*///				n = override_path;
    /*TODO*///				do
    /*TODO*///				{
    /*TODO*///					c = strchr(n,'\\');
    /*TODO*///					if (c) n = c+1;
    /*TODO*///				} while (c);
    /*TODO*///				strcpy(gamename,n);
    /*TODO*///				if (n == override_path)
    /*TODO*///					*n = 0;
    /*TODO*///				else
    /*TODO*///					*(n-1) = 0;
    /*TODO*///	
    /*TODO*///				/* strip out trailing extension */
    /*TODO*///				c = strchr(gamename,'.');
    /*TODO*///				if (c) *c = 0;
    /*TODO*///	
    /*TODO*///				for (i = 0; drivers[i] && (game_index == -1); i++)
    /*TODO*///				{
    /*TODO*///					if (stricmp(gamename,drivers[i]->name) == 0)
    /*TODO*///					{
    /*TODO*///						game_index = i;
    /*TODO*///						break;
    /*TODO*///					}
    /*TODO*///				}
    /*TODO*///	
    /*TODO*///				/* educated guess on what the user wants to play */
    /*TODO*///				if (game_index == -1)
    /*TODO*///				{
    /*TODO*///					int fuzz = 9999; /* best fuzz factor so far */
    /*TODO*///	
    /*TODO*///					for (i = 0; (drivers[i] != 0); i++)
    /*TODO*///					{
    /*TODO*///						int tmp;
    /*TODO*///						tmp = fuzzycmp(gamename, drivers[i]->description);
    /*TODO*///						/* continue if the fuzz index is worse */
    /*TODO*///						if (tmp > fuzz)
    /*TODO*///							continue;
    /*TODO*///	
    /*TODO*///						/* on equal fuzz index, we prefer working, original games */
    /*TODO*///						if (tmp == fuzz)
    /*TODO*///						{
    /*TODO*///							/* game is a clone */
    /*TODO*///							if (drivers[i]->clone_of != 0
    /*TODO*///									&& !(drivers[i]->clone_of->flags & NOT_A_DRIVER))
    /*TODO*///							{
    /*TODO*///								/* if the game we already found works, why bother. */
    /*TODO*///								/* and broken clones aren't very helpful either */
    /*TODO*///								if ((!drivers[game_index]->flags & GAME_NOT_WORKING) ||
    /*TODO*///									(drivers[i]->flags & GAME_NOT_WORKING))
    /*TODO*///									continue;
    /*TODO*///							}
    /*TODO*///							else continue;
    /*TODO*///						}
    /*TODO*///	
    /*TODO*///						/* we found a better match */
    /*TODO*///						game_index = i;
    /*TODO*///						fuzz = tmp;
    /*TODO*///					}
    /*TODO*///	
    /*TODO*///					if (game_index != -1)
    /*TODO*///						printf("fuzzy name compare, running %s\n",drivers[game_index]->name);
    /*TODO*///				}
    			}
    
	    		if (game_index == -1)
	    		{
	    			printf("Game \"%s\" not supported\n", argv[j]);
	    			return 1;
	    		}
		    }
	
    		if (settings.MESS){
                    /* This function has been added to MESS.C as load_image() */
                    load_image(argc, argv, j, game_index);
                }
    
    	/* parse generic (os-independent) options */
	    /*TODO*///parse_cmdline (argc, argv, game_index, override_path);
		    parse_cmdline (argc, argv, game_index);
    
    /*TODO*///	{	/* Mish:  I need sample rate initialised _before_ rom loading for optional rom regions */
    /*TODO*///		
    /*TODO*///		if (soundcard == 0) {    /* silence, this would be -1 if unknown in which case all roms are loaded */
    /*TODO*///			Machine->sample_rate = 0; /* update the Machine structure to show that sound is disabled */
    /*TODO*///			options.samplerate=0;
    /*TODO*///		}
    /*TODO*///	}
    /*TODO*///	
    /*TODO*///		/* handle record which is not available in mame.cfg */
    /*TODO*///		for (i = 1; i < argc; i++)
    /*TODO*///		{
    /*TODO*///			if (stricmp(argv[i],"-record") == 0)
    /*TODO*///			{
    /*TODO*///				i++;
    /*TODO*///				if (i < argc)
    /*TODO*///					options.record = osd_fopen(argv[i],0,OSD_FILETYPE_INPUTLOG,1);
    /*TODO*///			}
    /*TODO*///		}
    /*TODO*///	
    /*TODO*///	    if (options.record)
    /*TODO*///	    {
    /*TODO*///	        INP_HEADER inp_header;
    /*TODO*///	
    /*TODO*///	        memset(&inp_header, '\0', sizeof(INP_HEADER));
    /*TODO*///	        strcpy(inp_header.name, drivers[game_index]->name);
    /*TODO*///	        /* MAME32 stores the MAME version numbers at bytes 9 - 11
    /*TODO*///	         * MAME DOS keeps this information in a string, the
    /*TODO*///	         * Windows code defines them in the Makefile.
    /*TODO*///	         */
    /*TODO*///	        /*
    /*TODO*///	        inp_header.version[0] = 0;
    /*TODO*///	        inp_header.version[1] = VERSION;
    /*TODO*///	        inp_header.version[2] = BETA_VERSION;
    /*TODO*///	        */
    /*TODO*///	        osd_fwrite(options.record, &inp_header, sizeof(INP_HEADER));
    /*TODO*///	    }
    /*TODO*///	
    /*TODO*///		#ifdef MESS
    /*TODO*///		/* Build the CRC database filename */
    /*TODO*///		sprintf(crcfilename, "%s/%s.crc", crcdir, drivers[game_index]->name);
    /*TODO*///		if (drivers[game_index]->clone_of->name)
    /*TODO*///			sprintf (pcrcfilename, "%s/%s.crc", crcdir, drivers[game_index]->clone_of->name);
    /*TODO*///		else
    /*TODO*///			pcrcfilename[0] = 0;
    /*TODO*///	    #endif
    
        /* go for it */
    	res = run_game (game_index);
    	
    	/* close open files */
		if (errorlog != null) fclose (errorlog);
    /*TODO*///		if (options.playback) osd_fclose (options.playback);
    /*TODO*///		if (options.record)   osd_fclose (options.record);
    /*TODO*///		if (options.language_file) osd_fclose (options.language_file);

        
        return res;
    
    }
    
    public static void logerror(String str, Object... arguments) {
        if (errorlog != null) {
            fprintf(errorlog, str, arguments);
        }
    }
    /*TODO*///	void CLIB_DECL logerror(const char *text,...)
    /*TODO*///	{
    /*TODO*///		va_list arg;
    /*TODO*///		va_start(arg,text);
    /*TODO*///		if (errorlog)
    /*TODO*///			vfprintf(errorlog,text,arg);
    /*TODO*///		va_end(arg);
    /*TODO*///	}


}