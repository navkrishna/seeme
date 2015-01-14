package com.intelligrape.seeme.loader;

import com.intelligrape.seeme.model.Model;

/**
 * @author Rajendra
 *         <p/>
 *         Interface for async executor to get callback response
 *         object or collection of Model object
 */
public interface APICaller {
    public void onComplete(Model model);
}
