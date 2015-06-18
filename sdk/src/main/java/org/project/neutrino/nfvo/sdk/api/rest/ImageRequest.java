package org.project.neutrino.nfvo.sdk.api.rest;

import org.project.neutrino.nfvo.sdk.api.exception.SDKException;

import java.io.File;

/**
 * OpenBaton image-related api requester.
 */
public class ImageRequest extends Request {

    /**
     * Create a image requester with a given url path
     *
     * @param url
     * 				the url path used for the api requests
     */
    public ImageRequest(final String url) {
        super(url);
    }

    /**
     * Adds a new VNF software Image to the image repository
     *
     * @param image
     *            : Image to add
     * @return string: The image filled with values from the api
     */
	public String create (final File image) throws SDKException {

        String url = "http://localhost:8080/images";
        return post(url, image, "IMAGE CREATED");
	}

	/**
     * Removes the VNF software Image from the Image repository
     *
     * @param id
     *            : The Image's id to be deleted
     */
	public String delete(final String id) throws SDKException {
        String url = "http://localhost:8080/images";
        url = url + "/" + id;
        return delete(url, "IMAGE DELETED");
	}

    /**
     * Returns the list of the VNF software images available
     *
     * @return List<Image>: The list of VNF software images available
     */
	public String findAll() throws SDKException {
        String url = "http://localhost:8080/images";
        return get(url, "FOUND IMAGES");
	}

	/**
     * Returns the VNF software image selected by id
     *
     * @param id
     *            : The id of the VNF software image
     * @return image: The VNF software image selected
     */
	public String findById(final String id) throws SDKException {
        String url = "http://localhost:8080/images";
        url = url + "/" + id;
        return get(url, "FOUND IMAGE");
	}

    /**
     * Updates the VNF software image
     *
     * @param image
     *            : Image to add
     * @param id
     *            : the id of VNF software image
     * @return image: the VNF software image updated
     */
	public String update(final File image, final String id) throws SDKException {
        String url = "http://localhost:8080/images";
        url = url + "/" + id;
        return put(url, image, "IMAGE UPDATED");
	}

}
