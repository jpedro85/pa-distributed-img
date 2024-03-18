package Utils.Events.Enums;

/**
 * Enumerates the states of image processing within the system, reflecting the
 * lifecycle of an image from its initial receipt to the completion of
 * processing. This enumeration is utilized to manage and track the progress of
 * images as they move through various stages of processing, ensuring that each
 * step is completed before proceeding to the next.
 *
 * <p>
 * The {@code ImageStates} enum plays a critical role in coordinating the image
 * processing workflow, allowing the system to handle images efficiently and
 * effectively. It is particularly useful in systems where image processing
 * involves multiple, potentially asynchronous stages, such as message waiting,
 * processing, merging, and final storage or output.
 * </p>
 *
 * <p>
 * States:
 * </p>
 * <ul>
 * <li>{@code WAITING_FOR_MESSAGE} - Indicates that the system is waiting for an
 * initial message or signal to start processing the image. This state is
 * typically the entry point for new images entering the processing
 * pipeline.
 * </li>
 *
 * <li>{@code WAITING_FOR_PROCESSING} - Represents the state where the image is
 * queued for processing. The image has been received, but processing has not
 * yet started.
 * </li>
 *
 * <li>{@code PROCESSED} - Signifies that the image has been processed. This
 * state indicates completion of the primary image processing steps, but the
 * image may still require further actions such as merging with other images or
 * additional refinement.
 * </li>
 *
 * <li>{@code WAITING_FOR_MERGE} - Indicates that the image is waiting to be
 * merged with other processed images. This state is used in workflows where
 * multiple images are combined into a single output.
 * </li>
 *
 * <li>{@code MERGED} - Denotes that the image has been successfully merged with
 * other images. This is typically the final state in the processing workflow,
 * indicating that the image is now fully processed and ready for storage,
 * display, or further use.
 * </li>
 *
 * </ul>
 *
 * <p>
 * Using these states, the system can efficiently manage and monitor the
 * progress of images through the processing pipeline, addressing any
 * bottlenecks or errors that may arise during the workflow.
 * </p>
 */
public enum ImageStates {
    /**
     * The system is waiting for an initial message or signal to start processing
     * the image.
     */
    WAITING_FOR_MESSAGE,

    /**
     * The image is queued for processing but has not yet started the processing
     * phase.
     */
    WAITING_FOR_PROCESSING,

    /**
     * The image has been processed and is awaiting further actions such as merging.
     */
    PROCESSED,

    /**
     * The image is waiting to be merged with other processed images.
     */
    WAITING_FOR_MERGE,

    /**
     * The image has been successfully merged with other images and is fully
     * processed.
     */
    MERGED,
}
