# Entity Operations Checklist

## Entity name: GameResult

## Operations

In the list of operations below, check all the operations that apply. For example, if you know you will need to be able to insert a single instance of the entity at a time into the database, check **Single instance** in the **Create/insert** section.

Note that the pairs of square brackets below are rendered as checkboxes in GitHub Pages. To insert a checkmark, **replace** the single space between the square brackets in the Markdown with an "x" character (uppercase or lowercase; **do not** include the quote characters). To remove a checkmark, **replace** the "x" between the square brackets with a **single** space character. Aside from adding or removing checkmarks, do not modify the formatting or content of the remainder of this section.

### Create/insert
    
* [x] Single instance 
* [ ] Multiple instances 
    
### Read/query/select

* [ ] Single instance 
* [x] Multiple instances 

### Update

* [ ] Single instance 
* [ ] Multiple instances 

### Delete

* [ ] Single instance 
* [x] Multiple instances 


## Queries

For any queries (i.e. selecting from the database) that you think you will need to do with this entity, summarize the purpose of the query (what functionality in your application will use the query), whether the query is intended to return a single instance or multiple instances (and whether returning no instances is a valid possibility), what field(s)/column(s) of your entity will be used as filter criteria, and in what order the results (if multiple) should be returned.

Copy and paste the section below as many times as necessary, for all of the queries you currently anticipate implementing for this entity.

### All `GameResult` instances for a specified code length

Purpose

: Obtain the content supporting display of a high-scores screen.

Cardinality/modality

: Many(multiple game results for a specified code length), optional (nor results at all might have previously been recorded for a specified code length)
 
Filter

: Filtered on `code_length` column is equal to a specified code length.
 
Sort order

: `guess_count` (ascending), then `duration` (ascending).

