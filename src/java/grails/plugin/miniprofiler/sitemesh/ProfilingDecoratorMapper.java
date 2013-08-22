package grails.plugin.miniprofiler.sitemesh;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import io.jdev.miniprofiler.ProfilerProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

public class ProfilingDecoratorMapper implements DecoratorMapper {

	private final DecoratorMapper target;
	private final ProfilerProvider profilerProvider;

	public ProfilingDecoratorMapper(DecoratorMapper target, ProfilerProvider profilerProvider) {
		this.target = target;
		this.profilerProvider = profilerProvider;
	}

	@Override
	public void init(Config config, Properties properties, DecoratorMapper decoratorMapper) throws InstantiationException {
		target.init(config, properties, decoratorMapper);
	}

	@Override
	public Decorator getDecorator(HttpServletRequest httpServletRequest, Page page) {
		return wrapDecorator(target.getDecorator(httpServletRequest, page));
	}

	@Override
	public Decorator getNamedDecorator(HttpServletRequest httpServletRequest, String name) {
		return wrapDecorator(target.getNamedDecorator(httpServletRequest, name));
	}


	private Decorator wrapDecorator(Decorator decorator) {
		if(decorator == null) return null;
		return new ProfilingDecorator(decorator, profilerProvider.getCurrentProfiler());
	}
}
